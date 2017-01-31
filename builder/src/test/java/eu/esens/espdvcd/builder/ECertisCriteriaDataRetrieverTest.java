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
        // European Criterion Id
        // String criterionId = "d726bac9-e153-4e75-bfca-c5385587766d";
        
        // National Criterion Id - it origin
        String sourceId = "704390c8-34e3-4337-9ab1-f5795bed1a3b";
        
        // Valid Country Code
        // String targetCountryCode = "it";
        String targetCountryCode = "be";   
        
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        cdr.getNationalCriterionMapping(sourceId, targetCountryCode)
                .forEach(mappedCriterion -> 
                        System.out.println(
                                "Criterion with Id <<" + sourceId + ">>" 
                                + " Mapped to " +  cdr.getCountryNameByCountryCode(targetCountryCode) 
                                + " National Criterion with Id " + "<<" + mappedCriterion.getID().getValue() + ">>"));
        System.out.println("Criterion #: " + cdr.getNationalCriterionMapping(sourceId, targetCountryCode).size());
    }
   
    @Test
    public void testGetCriterion() {
        // A Valid Criterion Id (Can be a non-European Criterion Id)
        String criterionIds[] = {
            // eu
            "d726bac9-e153-4e75-bfca-c5385587766d",
            // national
            "b88e0b29-062c-4eb7-bdd4-f7292d20e53b"};
                        
        ECertisCriteriaExtractor cdr = new ECertisCriteriaExtractor();
        displayCriterion(cdr.getCriterion(criterionIds[0]));
        System.out.println();
        displayCriterion(cdr.getCriterion(criterionIds[1]));
    }
    
    private void displayCriterion(CriterionType ct) {
        System.out.printf("%-15s:%s%n", "Id" , ct.getID().getValue());
        System.out.printf("%-15s:%s%n", "Name", ct.getName().getValue());
        System.out.printf("%-15s:%s%n", "TypeCode" , ct.getTypeCode().getValue());
        System.out.printf("%-15s:%s%n", "SubCriterions #", ct.getSubCriterion().size());
    }
    
//    @Ignore
//    @Test
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
