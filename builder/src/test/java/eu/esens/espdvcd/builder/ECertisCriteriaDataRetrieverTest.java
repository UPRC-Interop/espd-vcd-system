package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import org.junit.Before;
import org.junit.Ignore;
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
        
    @Test
    public void testGetNationalCriterionMapping() {
        // An European Criterion Id
        String criterionId = "d726bac9-e153-4e75-bfca-c5385587766d";
        // Valid Country Code
        String countryCode = "it";

        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        cdr.getNationalCriterionMapping(criterionId, countryCode)
                .forEach(parentCriterion -> /*System.out.println(criterionType.getName().getValue()*/ {
                            parentCriterion.getSubCriterion().forEach((ct) -> {
                                System.out.println(ct.getName().getValue());
                            });
                        }
                );
        System.out.println("Criterion #: " + cdr.getNationalCriterionMapping(criterionId, countryCode).size());
    }

    @Ignore
    @Test
    public void testGetCriterion() {
        // A Valid Criterion Id (Can be a non-European Criterion Id)
        String criterionId = "65da1473-2667-4d79-8e3b-01c6c4f39db3";

        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        CriterionType ct = cdr.getCriterion(criterionId);
        System.out.println(ct.getName().getValue());
        System.out.println(ct.getTypeCode().getValue());
        System.out.println(ct.getSubCriterion().size());

        System.out.println("");

        System.out.println("is criterion with id " + criterionId + " an eu criterion? :" + cdr.isEuCriterionIdExists(criterionId));
        System.out.println("is criterion with id " + criterionId + " a criterion? : " + (cdr.getCriterion(criterionId) != null));
    }
    
    @Ignore
    @Test
    public void testGetEvidences() {
        // A Valid European Criterion
        String euCriterionId = "d726bac9-e153-4e75-bfca-c5385587766d";
        
        // A National Criterion Id 
        String nationalCriterionId = "65da1473-2667-4d79-8e3b-01c6c4f39db3";
        
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        cdr.getEvidences(euCriterionId);
        // cdr.getEvidences(nationalCriterionId).forEach(rg -> System.out.println(rg.getName().getValue()));
    }

}
