package eu.esens.espdvcd.model;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 */
public class SimpleESPDRequestTest {
    
    ESPDRequest req = new SimpleESPDRequest();
    
    @Before
    public void setUp() {
        // adding one selection, one exclusion, one EO Related to test the filters;
        LegislationReference LegRef = new LegislationReference(
                "Test Common Reference",
                "Test Reference description",
                "EU_DIRECTIVE",
                 "Test Article",
                "Test URI");
        
        SelectableCriterion selectionCrit = new SelectableCriterion(
                UUID.randomUUID().toString(),
                "SELECTION.SUITABILITY",
                "Test Selection", "Test Description", LegRef);
      
        SelectableCriterion exclusionCrit = new SelectableCriterion(
                UUID.randomUUID().toString(),
                "EXCLUSION.CRIMINAL_CONVICTIONS",
                "Test Exclusion", "Test Description", LegRef);
 
        SelectableCriterion eoCrit = new SelectableCriterion(
                UUID.randomUUID().toString(),
                "DATA_ON_ECONOMIC_OPERATOR",
                "Test EO Related", "Test Description", LegRef);
        
        SelectableCriterion rocCrit = new SelectableCriterion(
                UUID.randomUUID().toString(),
                "REDUCTION_OF_CANDIDATES",
                "Test Reduction of Candidates", "Test Description", LegRef);
        req.getFullCriterionList().add(selectionCrit);
        req.getFullCriterionList().add(exclusionCrit);
        req.getFullCriterionList().add(eoCrit);
        req.getFullCriterionList().add(rocCrit);
        
    }

    

    /**
     * Test of setCriterionList method, of class SimpleESPDRequest.
     */
    @Ignore
    @Test
    public void testSetCriterionList() {
        assertTrue(req.getFullCriterionList().size() == 4);
    }

    /**
     * Test of getSelectionCriteriaList method, of class SimpleESPDRequest.
     */
    @Ignore
    @Test
    public void testGetSelectionCriteriaList() {
        assertTrue("Size mismatch", req.getSelectionCriteriaList().size() == 1);
        assertTrue("Type mismatch", req.getSelectionCriteriaList().get(0).getTypeCode().contains("SELECTION"));       
    }

    /**
     * Test of getExclusionCriteriaList method, of class SimpleESPDRequest.
     */
    @Test
    @Ignore
    public void testGetExclusionCriteriaList() {
        assertTrue("Size mismatch", req.getExclusionCriteriaList().size() == 1);
        assertTrue("Type mismatch",req.getExclusionCriteriaList().get(0).getTypeCode().contains("EXCLUSION"));
    }

    /**
     * Test of getEORelatedCriteriaList method, of class SimpleESPDRequest.
     */
    @Test
    @Ignore
    public void testGetEORelatedCriteriaList() {
        assertTrue("Size mismatch", req.getEORelatedCriteriaList().size() == 1);
        assertTrue("Type mismatch", req.getEORelatedCriteriaList().get(0).getTypeCode().contains("DATA_ON_ECONOMIC_OPERATOR"));
    }
    
    @Test
    @Ignore
    public void testGetReductionOfCandidatesCriteriaList() {
        assertTrue("Size mismatch", req.getEORelatedCriteriaList().size() == 1);
        assertTrue("Type mismatch", req.getReductionOfCandidatesCriteriaList().get(0).getTypeCode().contains("REDUCTION_OF_CANDIDATES"));
    }
    
}
