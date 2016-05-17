/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author jerouris
 */
public class ModelBuilderTest {

    private InputStream isReq;
    private InputStream isReqReduced;
    private InputStream isRes;
    private InputStream isResReduced;
    
    public ModelBuilderTest() {
    }
    
     @Before
    public void setUp() {
        isReq = BuilderESPDTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(isReq);
        
        isRes = BuilderESPDTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(isRes);
        
        isReqReduced = BuilderESPDTest.class.getResourceAsStream("/espd-request-reduced.xml");
        Assert.assertNotNull(isReqReduced);
        
        isResReduced = BuilderESPDTest.class.getResourceAsStream("/espd-response-reduced.xml");
        Assert.assertNotNull(isResReduced);
    }

    @Test
    public void testBuilderEmptyRequest() {
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list is not empty",0, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderDefaultCriteriaRequest() {
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.addDefaultESPDCriteriaList().createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    @Test
    public void testBuilderImportRequestCreateRequest() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isReq).createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseCreateRequest() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isRes).createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportReducedRequestCreateRequest() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isReqReduced).createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",59, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedCreateRequest() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isResReduced).createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",60, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
      
    @Test
    public void testBuilderImportReducedRequestAddDefaultCriteriaCreateRequest() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isReqReduced).addDefaultESPDCriteriaList().createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
            assertEquals("Selected Criteria size is wrong",59,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedAddDefaultCriteriaCreateRequest() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isResReduced).addDefaultESPDCriteriaList().createESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
            assertEquals("Selected Criteria size is wrong",60,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderEmptyResponse() {
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.createESPDResponse();
            assertNotNull("ESPD Response is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("EO Details are null", req.getEODetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list is not empty",0, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderDefaultCriteriaResponse() {
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.addDefaultESPDCriteriaList().createESPDResponse();
            assertNotNull("ESPD Response is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("EO Details are null", req.getEODetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    @Test
    public void testBuilderImportRequestCreateResponse() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isReq).createESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("EO Details are null", req.getEODetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseCreateResponse() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isRes).createESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportReducedRequestCreateResponse() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isReqReduced).createESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
            assertEquals("Criteria list size is wrong",59, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedCreateResponse() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isResReduced).createESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
            assertEquals("Criteria list size is wrong",60, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
      
    @Test
    public void testBuilderImportReducedRequestAddDefaultCriteriaCreateResponse() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isReqReduced).addDefaultESPDCriteriaList().createESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
            assertEquals("Selected Criteria size is wrong",62,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedAddDefaultCriteriaCreateResponse() {
        
        ModelBuilder mb = new ModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isResReduced).addDefaultESPDCriteriaList().createESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
            assertEquals("Selected Criteria size is wrong",62,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }    
}
