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
public class OldModelBuilderTest {

    private InputStream isReq;
    private InputStream isReqReduced;
    private InputStream isRes;
    private InputStream isResReduced;
    
    public OldModelBuilderTest() {
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
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertEquals("Criteria list is not empty",0, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderDefaultCriteriaRequest() {
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.addDefaultESPDCriteriaList().createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    @Test
    public void testBuilderImportRequestCreateRequest() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isReq).createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseCreateRequest() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isRes).createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
  //          assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportReducedRequestCreateRequest() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isReqReduced).createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
 //           assertEquals("Criteria list size is wrong",59, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedCreateRequest() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isResReduced).createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",60, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
      
    @Test
    public void testBuilderImportReducedRequestAddDefaultCriteriaCreateRequest() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isReqReduced).addDefaultESPDCriteriaList().createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",63, req.getFullCriterionList().size());
//            assertEquals("Selected Criteria size is wrong",60,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedAddDefaultCriteriaCreateRequest() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDRequest req = mb.importFrom(isResReduced).addDefaultESPDCriteriaList().createRegulatedESPDRequest();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
//            assertEquals("Selected Criteria size is wrong",60,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderEmptyResponse() {
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.createRegulatedESPDResponse();
            assertNotNull("ESPD Response is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("EO Details are null", req.getEODetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list is not empty",0, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderDefaultCriteriaResponse() {
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.addDefaultESPDCriteriaList().createRegulatedESPDResponse();
            assertNotNull("ESPD Response is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("EO Details are null", req.getEODetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    @Test
    public void testBuilderImportRequestCreateResponse() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isReq).createRegulatedESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("EO Details are null", req.getEODetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseCreateResponse() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isRes).createRegulatedESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportReducedRequestCreateResponse() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isReqReduced).createRegulatedESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
//            assertEquals("Criteria list size is wrong",59, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedCreateResponse() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isResReduced).createRegulatedESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
//            assertEquals("Criteria list size is wrong",60, req.getFullCriterionList().size());
        } catch (BuilderException e) {
            
        }
    }
      
    @Test
    public void testBuilderImportReducedRequestAddDefaultCriteriaCreateResponse() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isReqReduced).addDefaultESPDCriteriaList().createRegulatedESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
            assertNotNull("EO Details are null", req.getEODetails());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
//            assertEquals("Selected Criteria size is wrong",62,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }
    
    @Test
    public void testBuilderImportResponseReducedAddDefaultCriteriaCreateResponse() {
        
        OldModelBuilder mb = new OldModelBuilder();
        try {
            ESPDResponse req = mb.importFrom(isResReduced).addDefaultESPDCriteriaList().createRegulatedESPDResponse();
            assertNotNull("ESPD Request is Null", req);
            assertNotNull("CA Details are null", req.getCADetails());
            assertNotNull("Criteria list is null", req.getFullCriterionList());
//            assertEquals("Criteria list size is wrong",62, req.getFullCriterionList().size());
//            assertEquals("Selected Criteria size is wrong",62,req.getFullCriterionList().stream().filter(c->c.isSelected()).toArray().length);
        } catch (BuilderException e) {
            
        }
    }    
}