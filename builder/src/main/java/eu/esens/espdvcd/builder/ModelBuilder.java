package eu.esens.espdvcd.builder;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.SimpleESPDRequest;
import eu.esens.espdvcd.model.SimpleESPDResponse;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.PredefinedESPDCriteriaExtractor;
import eu.esens.espdvcd.schema.SchemaUtil;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;


public class ModelBuilder {
    
    private List<SelectableCriterion> criteriaList;
    private EODetails eoDetails;
    private CADetails caDetails;
    private CriteriaExtractor criteriaExtractor;
    
    private boolean includeDefaultCriteriaAsUnselected = true;
    
    public ModelBuilder() {
        
        // Default initialization of the ESPDRequest and ESPDResponse Models.
        // Empty CADetails
        caDetails = new CADetails();
        
        // Empty EODetails (with initialized lists)
        eoDetails = new EODetails();
        eoDetails.setContactingDetails(new ContactingDetails());
        eoDetails.setNaturalPersons(new VirtualFlow.ArrayLinkedList<>());
        eoDetails.getNaturalPersons().add(new NaturalPerson());
        
        // Empty Criteria List
        criteriaList = new ArrayList<>();
        
        //Setting critera extractor to null will leave the criteria list empty
        criteriaExtractor = null;
    
    };
  
    public ModelBuilder withCADetailsFrom(InputStream is) throws BuilderException {
    
        ESPDRequest req;

        try (InputStream bis = getBufferedInputStream(is)) {
            // Check and read the file in the JAXB Object
            ESPDRequestType reqType = readESPDRequestFromStream(bis);
            // Create the Model Object
            req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
            caDetails = req.getCADetails();

        } catch (IOException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream for ESPD Request", ex);
        }

        return this;   
    }
    
    public ModelBuilder withCADetailsFrom(ESPDRequest req) {
        caDetails = req.getCADetails();
        return this;
    }
    
    public ModelBuilder withCADetailsFrom(CADetails caDetails) {
        this.caDetails = caDetails;
        return this;
    }
    
    public ModelBuilder withEODetailsFrom(InputStream is) throws BuilderException {
           ESPDResponse res;
        // Check and read the file in the JAXB Object
        try (InputStream bis = getBufferedInputStream(is)) {
            // Check and read the file in the JAXB Object
            ESPDResponseType resType = readESPDResponseFromStream(bis);
            // Create the Model Object
            res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(resType);
            eoDetails = res.getEoDetails();
            
        } catch (IOException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }
    
        return this;   
    }
    
    public ModelBuilder withEODetailsFrom(ESPDResponse res) {
        eoDetails = res.getEoDetails();
        return this;
    }
    
    public ModelBuilder withEODetailsFrom(EODetails eoDetails) {
        this.eoDetails = eoDetails;
        return this;
    }
    
    public ModelBuilder withDefaultESPDCriteriaList() {

        criteriaExtractor  = new PredefinedESPDCriteriaExtractor();
        return this;       
    }
    
    public ESPDRequest createESPDRequest() {
        ESPDRequest req = new SimpleESPDRequest();
        req.setCADetails(caDetails);
        req.setCriterionList(criteriaList);
        return req;
    }
    
    public ESPDResponse createESPDResponse() {
        ESPDResponse res = new SimpleESPDResponse();
        res.setCADetails(caDetails);
        res.setCriterionList(criteriaList);
        res.setEODetails(eoDetails);
        return res;
    }
        
    private InputStream getBufferedInputStream(InputStream xmlESPD) {
    // We require a marked input stream
    InputStream bis;
    if (xmlESPD.markSupported()) {
        bis = xmlESPD;
    } else {
        bis = new BufferedInputStream(xmlESPD);
    }
        return bis;
    }
    
    private List<SelectableCriterion> getCriteriaList() {
        CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
        return cr.getFullList();
    }

    
    private ESPDRequestType readESPDRequestFromStream(InputStream is) {
        try {
            // Start with the convience methods provided by JAXB. If there are
            // perfomance issues we will swicth back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), ESPDRequestType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private ESPDResponseType readESPDResponseFromStream(InputStream is) {
        try {
            // Start with the convience methods provided by JAXB. If there are
            // perfomance issues we will swicth back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), ESPDResponseType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
