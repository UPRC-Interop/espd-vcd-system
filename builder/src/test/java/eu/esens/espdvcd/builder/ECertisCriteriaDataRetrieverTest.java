package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaDataRetrieverTest {

    public ECertisCriteriaDataRetrieverTest() {
    }

    @Before
    public void setUp() {
    }
        
//    @Ignore
    @Test
    public void testGetNationalCriterionMapping() {
        // European Criterion Id
        //String sourceId = "d726bac9-e153-4e75-bfca-c5385587766d";
        //String sourceId = "d726bac9-e153-4e75-bfca-c5385587766dadawd";
        
        // National Criterion Id - it origin
        String sourceId = "704390c8-34e3-4337-9ab1-f5795bed1a3b";
        
        // Valid Country Code
        // String targetCountryCode = "it";
        String targetCountryCode = "be";   
        // String targetCountryCode = "bdadada";   
        
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        List<CriterionType> mappedCriterions = new ArrayList<>();       
        
        try {
            mappedCriterions = cdr.getNationalCriterionMapping(sourceId, targetCountryCode);
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
        mappedCriterions.forEach(mappedCriterion -> 
                        System.out.println(
                                "Criterion with Id <<" + sourceId + ">> Mapped to " + targetCountryCode.toUpperCase()
                                + " National Criterion with Id " 
                                + "<<" + mappedCriterion.getID().getValue() + ">>"));
        System.out.println("Criterion #: " + mappedCriterions.size());
    }
    
   
    @Test
    public void testGetCriterion() {
        // A Valid Criterion Id (Can be a non-European Criterion Id)
        String criterionIds[] = {
            // eu
            "d726bac9-e153-4e75-bfca-c5385587766d",
            // national
            "b88e0b29-062c-4eb7-bdd4-f7292d20e53b"
            // not Valid Criteria Id
            /*"acdc"*/};
                        
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
    
//    @Ignore
//    @Test
//    public void testGetEvidences() {
//        // A Valid European Criterion
//        String euCriterionId = "d726bac9-e153-4e75-bfca-c5385587766d";
//        
//        // A National Criterion Id 
//        String nationalCriterionId = "65da1473-2667-4d79-8e3b-01c6c4f39db3";
//        
//        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
//        cdr.getEvidences(euCriterionId);
//        // cdr.getEvidences(nationalCriterionId).forEach(rg -> System.out.println(rg.getName().getValue()));
//    }

}
