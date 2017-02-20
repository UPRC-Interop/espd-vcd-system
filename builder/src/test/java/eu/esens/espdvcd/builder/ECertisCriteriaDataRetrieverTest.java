package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaDataRetrieverTest {

    private final String[] criterionIds = {
            // eu
            "d726bac9-e153-4e75-bfca-c5385587766d",
            // national - italian origin
            "704390c8-34e3-4337-9ab1-f5795bed1a3b"};
       
    public ECertisCriteriaDataRetrieverTest() {
    }

    @Before
    public void setUp() {
    }
    
    @Ignore
    @Test
    public void testGetAllNationalEntities() {
        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
        try {
            extractor.getAllNationalEntities().forEach(ne -> System.out.println(ne));
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }
    
    @Ignore
    @Test
    public void testGetAllEuropeanCriteriaIds() {
        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
        try {
            List<String> ids = extractor.getAllEuropeanCriteriaIds();
            ids.forEach(id -> {
                try {
                    System.out.println(extractor.getCriterion(id).getID().getValue());
                } catch (RetrieverException ex) {
                    System.err.println(ex);
                }
            });
            System.out.println("Number of eu criteria is : " + ids.size());
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }
    
    @Ignore
    @Test
    public void testGetCriterionNationality() {
        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
        try {
            // CriterionType c = extractor.getCriterion("9fc69031-5b4d-440a-828c-2167e6f22328");
            System.out.println(
                extractor.getJurisdictionLevelCodeFromRequirementGroup("a34b70d6-c43d-4726-9a88-8e2b438424bf")
            );
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }
        
    @Test
    public void testGetNationalCriterionMappings() {
//        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
//        try {
//             extractor.getAllNationalEntities().forEach(ne -> testGetNationalCriterionMapping(ne.getId()));
            testGetNationalCriterionMapping("cz");
//        } catch (RetrieverException ex) {
//            System.err.println(ex);
//        }
    }    
 
    public void testGetNationalCriterionMapping(String countryCode) {
        
        ECertisCriteriaExtractor extractor = new ECertisCriteriaExtractor();
        String countryName = Codelists.CountryIdentification.getValueForId(countryCode.toUpperCase());
        
//        String[] ids = {"a34b70d6-c43d-4726-9a88-8e2b438424bf",
//                        "9fc69031-5b4d-440a-828c-2167e6f22328"};
        
        try {
            
            int total = 0;
            
            System.out.printf("Mapping European Criteria to %s National Criteria%n%n", countryName);
            System.out.printf("%-50s%-50s%n%n", "European Criterion", "National Criterion");
            
            for (String id : extractor.getAllEuropeanCriteriaIds()) {
                List<CriterionType> mappedCriterions = extractor.getNationalCriterionMapping(id, countryCode);
                
                System.out.printf("%-50s", id);
                boolean first = true;
                
                for (CriterionType c : mappedCriterions) {
                    
                    if (first) {
                        System.out.printf("%-50s%n", c.getID().getValue());
                        first = false;
                    } else {
                        System.out.printf("%-50s%-50s%n", " ", c.getID().getValue());
                    }
                    
                    total++;
                }
                
                System.out.printf("%n%n");
            }
            
            System.out.println("Total Mappings : " + total);
                       
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
        
    }
    
    @Ignore
    @Test
    public void testGetCriterion() {
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        try {
            for (String ctId : criterionIds) {
                displayCriterion(cdr.getCriterion(ctId));
                System.out.println();
            }
            
        } catch (RetrieverException e) {
            System.err.println(e);
        }
    }
    
    private void displayCriterion(CriterionType ct) {
        System.out.printf("%-25s:%s%n", "Id" , ct.getID().getValue());
        System.out.printf("%-25s:%s%n", "Name", ct.getName().getValue());
        System.out.printf("%-25s:%s%n", "TypeCode" , ct.getTypeCode().getValue());
        if (!ct.getLegislationReference().isEmpty()) {
            System.out.printf("%-25s:%s%n", "JurisdictionLevelCode" , ct.getLegislationReference().get(0)
                    .getJurisdictionLevelCode().getValue());
        }
        System.out.printf("%-25s:%s%n", "SubCriterions #", ct.getSubCriterion().size());
    }
     
    @Ignore
    @Test
    public void testGetEvidences() {
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        
        try {
            for (String ctId : criterionIds) {
                System.out.print("Displaying Evidences for Criterion with Id <<" + ctId + ">> :");
                List<RequirementGroupType> evidences = cdr.getEvidences(ctId);
                evidences.forEach(rgt -> displayRequirementGroupType(rgt));
                System.out.println();
            }
            
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }

    private void displayRequirementGroupType(RequirementGroupType rgt) {
        System.out.print(rgt.getID().getValue());
    }
    
}
