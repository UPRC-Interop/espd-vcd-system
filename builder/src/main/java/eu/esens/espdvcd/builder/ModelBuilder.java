package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;
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

/**
 * The ModelBuilder is a builder pattern implemented class, that is used for
 * guided creation of ESPD Model POJOs.
 * 
 * @since 1.0
 */
public class ModelBuilder {
    
    private EODetails eoDetails = null;
    private CADetails caDetails = null;
    private CriteriaExtractor criteriaExtractor = null;
    private InputStream importStream = null;
   
    /**
     * Loads from an ESPD Request or an ESPD Response all the required data and
     * are used as the defaults for the creation of the ESPD(Request/Response) POJO.
     * 
     * @param is The input stream that will be read to create the Model POJO. The
     * input stream must point to a valid ESPD Request or ESPD Response XML Artefact
     * 
     * @return the same ModelBuilder instance for incremental creation of the required object.
     */
    public ModelBuilder importFrom(InputStream is) {
        importStream = getBufferedInputStream(is);
        return this;        
    }
    
    /**
     * Overrides the CA Details of the created Model POJO with the ones found in the
     * Provided input stream.
     * 
     * @param is The input stream that will be read to extract the CA Details from. The
     * input stream must point to a valid ESPD Request or ESPD Response XML Artefact
     * 
     * @return the same ModelBuilder instance for incremental creation of the required object.
 
     * @throws BuilderException if the input stream is on a valid ESPD Request or Response;
     */
    public ModelBuilder withCADetailsFrom(InputStream is) throws BuilderException {
        
        ESPDRequest req = createESPDRequestFromXML(is);
        caDetails = req.getCADetails();
        return this;        
    }
    
    /**
     * Overrides the CA Details of the created Model POJO with the ones found in the
     * Provided input stream.
     * 
     * @param caDetails The {@link CADetails} object hat will override the CA Details of 
     * the created object
     * 
     * @return the same ModelBuilder instance for incremental creation of the required object.
     * 
     */
    public ModelBuilder withCADetailsFrom(CADetails caDetails) {
        this.caDetails = caDetails;
        return this;
    }
    
    /**
     * Overrides the EO Details of the created Model POJO with the ones found in the
     * Provided input stream.
     * 
     * @param is The input stream that will be read to extract the EO Details from. The
     * input stream must point to a valid ESPD Request or ESPD Response XML Artefact
     * 
     * @return the same ModelBuilder instance for incremental creation of the required object.
 
     * @throws BuilderException if the input stream is on a valid ESPD Request or Response;
     */
    public ModelBuilder withEODetailsFrom(InputStream is) throws BuilderException {
        ESPDResponse res = createESPDResponseFromXML(is);
        eoDetails = res.getEODetails();        
        return this;        
    }
    
    /**
     * Overrides the CA Details of the created Model POJO with the ones found in the
     * Provided input stream.
     * 
     * @param eoDetails The {@link EODetails} object hat will override the EO Details of 
     * the created object
     * 
     * @return the same ModelBuilder instance for incremental creation of the required object.
     * 
     */
    public ModelBuilder withEODetailsFrom(EODetails eoDetails) {
        this.eoDetails = eoDetails;
        return this;
    }
    
    /**
     * Adds the default criteria list of the ESPD Form as it is defined by the 
     * European Commission.<br>
     * 
     * If the created object is an new (not imported) ESPD Request, then the criteria
     * added are pre-selected.<br>
     * If the created object comes from an imported ESPD Artefact, then the default
     * extra criteria added, are included as non-selected. <br>     * 
     * If the created object is an ESPD Response, then the criteria added will always
     * be pre-selected.
     * 
     * @return the same ModelBuilder instance for incremental creation of the required object.
     */
    public ModelBuilder addDefaultESPDCriteriaList() {
        
        criteriaExtractor = new PredefinedESPDCriteriaExtractor();
        return this;        
    }
    
    /**
     * Terminal builder method that returns an {@link ESPDRequest} instance,
     * @return the created ESPD Request
     * @throws BuilderException if the import failed.
     */
    public ESPDRequest createESPDRequest() throws BuilderException {
        ESPDRequest req;
        if (importStream != null) {
            req = createESPDRequestFromXML(importStream);
            if (criteriaExtractor != null) {
                req.setCriterionList(criteriaExtractor.getFullList(req.getFullCriterionList()));
            }
        } else {
            req = new SimpleESPDRequest();
            if (criteriaExtractor != null) {
                req.setCriterionList(criteriaExtractor.getFullList());
            } else {
                req.setCriterionList(getEmptyCriteriaList());
            }
            req.setCADetails(createDefaultCADetails());
        }

        //Overriding the default/imported ca details
        if (caDetails != null) {
            req.setCADetails(caDetails);
        }
        
        // Apply workaround
        req.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        
        return req;
    }
    
    /**
     * Terminal builder method that returns an {@link ESPDResponse} instance,
     * @return the created ESPD Response
     * @throws BuilderException if the import failed.
     */
    public ESPDResponse createESPDResponse() throws BuilderException {
        
        ESPDResponse res;
        if (importStream != null) {
            res = createESPDResponseFromXML(importStream);
            if (criteriaExtractor != null) {
                res.setCriterionList(criteriaExtractor.getFullList(res.getFullCriterionList(), true));
            }
        } else {
            res = new SimpleESPDResponse();
            if (criteriaExtractor != null) {
                res.setCriterionList(criteriaExtractor.getFullList());
            } else {
                res.setCriterionList(getEmptyCriteriaList());
            }
     

        }
        
        if (caDetails != null) {
            res.setCADetails(caDetails);
        } else {
                   res.setCADetails(createDefaultCADetails());           
        }
        
        if (eoDetails != null) {
            res.setEODetails(eoDetails);

        } else { 
                        res.setEODetails(createDefaultEODetails());
        }
        
                // Apply workaround
        res.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        
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
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private ESPDResponseType readESPDResponseFromStream(InputStream is) {
        try {
            // Start with the convience methods provided by JAXB. If there are
            // perfomance issues we will swicth back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), ESPDResponseType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Parses the input stream and creates an ESPDRequest model instance.
     *
     * @param xmlESPD The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDRequest Model
     * fails
     */
    private ESPDRequest createESPDRequestFromXML(InputStream xmlESPD) throws BuilderException {
        
        ESPDRequest req;
        
        try (InputStream bis = getBufferedInputStream(xmlESPD)) {
            // Check and read the file in the JAXB Object
            ESPDRequestType reqType = readESPDRequestFromStream(bis);
            // Create the Model Object
            req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
            
            return req;
            
        } catch (IOException ex) {
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream", ex);
        }
        
    }

    /**
     * Parses the input stream and creates an ESPDResponse model instance.
     *
     * @param xmlESPDRes The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDResponse Model
     * fails
     */
    private ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {
        
        ESPDResponse res;
        // Check and read the file in the JAXB Object
        try (InputStream bis = getBufferedInputStream(xmlESPDRes)) {
            // Check and read the file in the JAXB Object
            ESPDResponseType resType = readESPDResponseFromStream(bis);
            // Create the Model Object
            res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(resType);
        } catch (IOException ex) {
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }
        
        return res;
    }
    
    private EODetails createDefaultEODetails() {
        // Empty EODetails (with initialized lists)
        System.out.println("Creating default EO Details");
        EODetails eod = new EODetails();
        eod.setContactingDetails(new ContactingDetails());
        eod.setPostalAddress(new PostalAddress());
        eod.setNaturalPersons(new ArrayList<>());

        NaturalPerson np = new NaturalPerson();
        np.setPostalAddress(new PostalAddress());
        np.setContactDetails(new ContactingDetails());

        eod.getNaturalPersons().add(np);
        return eod;
    }
    
    private CADetails createDefaultCADetails() {
        // Default initialization of the ESPDRequest and ESPDResponse Models.
        // Empty CADetails
        
        return new CADetails();
        
    }
    
    private List<SelectableCriterion> getEmptyCriteriaList() {
        // Empty Criteria List
        return new ArrayList<>();
    }
    
    private void applyCriteriaWorkaround(Criterion c) {
        
//        if (c.getTypeCode().equals("SELECTION.ECONOMIC_FINANCIAL_STANDING") 
//                || c.getTypeCode().equals("DATA_ON_ECONOMIC_OPERATOR")) {
            if (c.getDescription().equals("")) {
              String oldName = c.getName();
              c.setDescription(oldName);
              // Since we have no name, we will add the Criteria type name as Criterion Name
              c.setName(Codelists.CriteriaType.getValueForId(c.getTypeCode()) +" (No Name)");
//                System.out.println("Workaround for: "+c.getID() +" "+c.getDescription());
            }
//        }      
    }
}
