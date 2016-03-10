/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.SimpleESPDRequest;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import javax.xml.bind.JAXB;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public class ESPDCriteriaExtractorTest {

    static int maxDepth = 1;
    public ESPDCriteriaExtractorTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of getFullList method, of class ESPDCriteriaExtractor.
     */
    @Ignore
    @Test
    public void testGetFullList() {
        ESPDCriteriaExtractor ce = new ESPDCriteriaExtractor();
        ce.getFullList().stream()
                .forEach(c -> {
                    System.out.println(c.getID() + " " + c.getName() + " (" + c.getTypeCode() + ")");
                    c.getRequirementGroups().forEach(rg -> traverseRequirementGroup(rg,1));
                });
        System.out.println("Max Depth: "+maxDepth);
    }

    private void traverseRequirementGroup(RequirementGroup rg, int depth) {

        if (depth > maxDepth) {
           maxDepth = depth;
        }
        
        String tabs = "";
        for (int i=0; i<depth; i++) {
            tabs +="\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs+"RequirementGroup: " + rg.getID());
        System.out.println(tabs+"Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs+"\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth+1;
        rg.getRequirementGroups().forEach(rg1 -> traverseRequirementGroup(rg1, innerDepth));
    }
    
    @Test
    public void loadTransformAndDisplayTest() {
        
        ESPDRequestType reqType = JAXB.unmarshal(ESPDCriteriaExtractorTest.class.getResourceAsStream("/espd-request.xml"),ESPDRequestType.class);
        SimpleESPDRequest req = ModelFactory.extractESPDRequest(reqType);
        
        ESPDRequestType req2Type = SchemaFactory.extractESPDRequestType(req);
        JAXB.marshal(req2Type, System.out);
        
    }
}
