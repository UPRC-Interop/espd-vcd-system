/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.model;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
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
        req.getFullCriterionList().add(selectionCrit);
        req.getFullCriterionList().add(exclusionCrit);
        req.getFullCriterionList().add(eoCrit);
        
    }

    

    /**
     * Test of setCriterionList method, of class SimpleESPDRequest.
     */
    @Test
    public void testSetCriterionList() {
        assertTrue(req.getFullCriterionList().size() == 3);
    };

    /**
     * Test of getSelectionCriteriaList method, of class SimpleESPDRequest.
     */
    @Test
    public void testGetSelectionCriteriaList() {
        assertTrue("Size mismatch", req.getSelectionCriteriaList().size() == 1);
        assertTrue("Type mismatch", req.getSelectionCriteriaList().get(0).getTypeCode().contains("SELECTION"));
    }

    /**
     * Test of getExclusionCriteriaList method, of class SimpleESPDRequest.
     */
    @Test
    public void testGetExclusionCriteriaList() {
        assertTrue("Size mismatch", req.getExclusionCriteriaList().size() == 1);
        assertTrue("Type mismatch",req.getExclusionCriteriaList().get(0).getTypeCode().contains("EXCLUSION"));
    }

    /**
     * Test of getEORelatedCriteriaList method, of class SimpleESPDRequest.
     */
    @Test
    public void testGetEORelatedCriteriaList() {
        assertTrue("Size mismatch", req.getEORelatedCriteriaList().size() == 1);
        assertTrue("Type mismatch", req.getEORelatedCriteriaList().get(0).getTypeCode().contains("DATA_ON_ECONOMIC_OPERATOR"));
    }
    
}
