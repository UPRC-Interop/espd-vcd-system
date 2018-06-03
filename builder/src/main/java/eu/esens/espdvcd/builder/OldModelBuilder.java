package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
//import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
//import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ModelBuilder is a builder pattern implemented class, that is used for
 * guided creation of ESPD Model POJOs.
 *
 * @since 1.0
 */
public class OldModelBuilder implements ModelBuilder {

    private EODetails eoDetails = null;
    private CADetails caDetails = null;
    private ServiceProviderDetails serviceProviderDetails = null;
    private CriteriaExtractor criteriaExtractor = null;
    private InputStream importStream = null;

    /* package private constructor. Create only through factory */
    OldModelBuilder() {}

    /**
     * Loads from an ESPD Request or an ESPD Response all the required data and
     * are used as the defaults for the creation of the ESPD(Request/Response)
     * POJO.
     *
     * @param is The input stream that will be read to create the Model POJO.
     * The input stream must point to a valid ESPD Request or ESPD Response XML
     * Artefact
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     */
    public OldModelBuilder importFrom(InputStream is) {
        importStream = ArtefactUtils.getBufferedInputStream(is);
        return this;
    }

    /**
     * Overrides the CA Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param is The input stream that will be read to extract the CA Details
     * from. The input stream must point to a valid ESPD Request or ESPD
     * Response XML Artefact
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     *
     * @throws BuilderException if the input stream is on a valid ESPD Request
     * or Response;
     */
    public OldModelBuilder withCADetailsFrom(InputStream is) throws BuilderException {

        ESPDRequest req = createRegulatedESPDRequestFromXML(is);
        caDetails = req.getCADetails();
        return this;
    }

    /**
     * Overrides the CA Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param caDetails The {@link CADetails} object hat will override the CA
     * Details of the created object
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     *
     */
    public OldModelBuilder withCADetailsFrom(CADetails caDetails) {
        this.caDetails = caDetails;
        return this;
    }

    /**
     * Overrides the EO Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param is The input stream that will be read to extract the EO Details
     * from. The input stream must point to a valid ESPD Request or ESPD
     * Response XML Artefact
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     *
     * @throws BuilderException if the input stream is on a valid ESPD Request
     * or Response;
     */
    public OldModelBuilder withEODetailsFrom(InputStream is) throws BuilderException {
        ESPDResponse res = createRegulatedESPDResponseFromXML(is);
        eoDetails = res.getEODetails();
        return this;
    }

    /**
     * Overrides the CA Details of the created Model POJO with the ones found in
     * the Provided input stream.
     *
     * @param eoDetails The {@link EODetails} object hat will override the EO
     * Details of the created object
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     *
     */
    public OldModelBuilder withEODetailsFrom(EODetails eoDetails) {
        this.eoDetails = eoDetails;
        return this;
    }

    /**
     * Adds the default criteria list of the ESPD Form as it is defined by the
     * European Commission.<br>
     *
     * If the created object is an new (not imported) ESPD Request, then the
     * criteria added are pre-selected.<br>
     * If the created object comes from an imported ESPD Artefact, then the
     * default extra criteria added, are included as non-selected. <br> * If the
     * created object is an ESPD Response, then the criteria added will always
     * be pre-selected.
     *
     * @return the same ModelBuilder instance for incremental creation of the
     * required object.
     */
//    public OldModelBuilder addDefaultESPDCriteriaList() {
//
//        criteriaExtractor = new PredefinedESPDCriteriaExtractor();
//        return this;
//    }

    /**
     * Terminal builder method that returns an {@link ESPDRequest} instance,
     *
     * @return the created ESPD Request
     * @throws BuilderException
     * @deprecated as of release 2.0.2, replaced by {@link #createRegulatedESPDRequest()}
     */
    @Deprecated
    public ESPDRequest createESPDRequest() throws BuilderException {
        return createRegulatedESPDRequest();
    }

    /**
     * Terminal builder method that returns an {@link ESPDRequest} instance,
     *
     * @return the created ESPD Request
     * @throws BuilderException if the import failed.
     */
    public ESPDRequest createRegulatedESPDRequest() throws BuilderException {
        ESPDRequest req;
        if (importStream != null) {
            req = createRegulatedESPDRequestFromXML(importStream);
            if (criteriaExtractor != null) {
                try {
                    req.setCriterionList(criteriaExtractor.getFullList(req.getFullCriterionList()));
                } catch (RetrieverException ex) {
                    Logger.getLogger(OldModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            req = new RegulatedESPDRequest();
            handleCriteriaExtractor(req);
            req.setCADetails(createDefaultCADetails());
            req.setServiceProviderDetails(createDefaultServiceProviderDetails());
        }

        //Overriding the default/imported ca details
        if (caDetails != null) {
            req.setCADetails(caDetails);
        }

        // Overriding the default/imported service provider details
        if (serviceProviderDetails != null) {
            req.setServiceProviderDetails(serviceProviderDetails);
        }

        // Apply workaround
        req.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        return req;
    }

    private void handleCriteriaExtractor(ESPDRequest req) {
        if (criteriaExtractor != null) {
            try {
                req.setCriterionList(criteriaExtractor.getFullList());
            } catch (RetrieverException ex) {
                Logger.getLogger(OldModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            req.setCriterionList(getEmptyCriteriaList());
        }
    }

    /**
     * Terminal builder method that returns an {@link ESPDResponse} instance,
     *
     * @return the created ESPD Response
     * @throws BuilderException if the import failed.
     * @deprecated as of release 2.0.2, replaced by {@link #createRegulatedESPDResponse()}
     */
    @Deprecated
    public ESPDResponse createESPDResponse() throws BuilderException {
        return createRegulatedESPDResponse();
    }

    /**
     * Terminal builder method that returns an {@link ESPDResponse} instance,
     *
     * @return the created ESPD Response
     * @throws BuilderException if the import failed.
     */
    public ESPDResponse createRegulatedESPDResponse() throws BuilderException {

        ESPDResponse res;
        if (importStream != null) {
            res = createRegulatedESPDResponseFromXML(importStream);
            if (criteriaExtractor != null) {
                try {
                    res.setCriterionList(criteriaExtractor.getFullList(res.getFullCriterionList(), true));
                } catch (RetrieverException ex) {
                    Logger.getLogger(OldModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            res = new RegulatedESPDResponse();
            handleCriteriaExtractor(res);
        }

        if (res.getCADetails() == null) {
            res.setCADetails(createDefaultCADetails());
        }
        if (res.getEODetails() == null) {
            res.setEODetails(createDefaultEODetails());
        }
        if (res.getServiceProviderDetails() == null) {
            res.setServiceProviderDetails(createDefaultServiceProviderDetails());
        }

        if (caDetails != null) {
            res.setCADetails(caDetails);
        }

        if (eoDetails != null) {
            res.setEODetails(eoDetails);
        }

        if (serviceProviderDetails != null) {
            res.setServiceProviderDetails(serviceProviderDetails);
        }

        // Apply workaround
        res.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        return res;
    }

    private ESPDRequestType readRegulatedESPDRequestFromStream(InputStream is)
        throws JAXBException {

            // Start with the convenience methods provided by JAXB. If there are
            // performance issues we will switch back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller(SchemaVersion.V1).unmarshal(new StreamSource(is), ESPDRequestType.class).getValue();

    }

    /**
     * @deprecated as of release 2.0.2, replaced by {@link #readRegulatedESPDResponseFromStream(InputStream)}
     */
    @Deprecated
    protected ESPDResponseType readESPDResponseFromStream(InputStream is) {
        return readRegulatedESPDResponseFromStream(is);
    }

    protected ESPDResponseType readRegulatedESPDResponseFromStream(InputStream is) {
        try {
            // Start with the convenience methods provided by JAXB. If there are
            // performance issues we will switch back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller(SchemaVersion.V1).unmarshal(new StreamSource(is), ESPDResponseType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(OldModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
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
    private ESPDRequest createRegulatedESPDRequestFromXML(InputStream xmlESPD) throws BuilderException {

        ESPDRequest req;

        try (InputStream bis = ArtefactUtils.getBufferedInputStream(xmlESPD)) {
            // Check and read the file in the JAXB Object
            ESPDRequestType reqType = readRegulatedESPDRequestFromStream(bis);
            // Create the Model Object
            req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
            return req;

        } catch (IOException | JAXBException ex) {
            Logger.getLogger(OldModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream", ex);
        }

    }

    /**
     * @deprecated as of release 2.0.2, replaced by {@link #createRegulatedESPDResponseFromXML(InputStream)}
     */
    @Deprecated
    protected ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {
        return createRegulatedESPDResponseFromXML(xmlESPDRes);
    }

    /**
     * Parses the input stream and creates an ESPDResponse model instance.
     *
     * @param xmlESPDRes The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDResponse Model
     * fails
     */
    protected ESPDResponse createRegulatedESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {

        ESPDResponse res;
        // Check and read the file in the JAXB Object
        try (InputStream bis = ArtefactUtils.getBufferedInputStream(xmlESPDRes)) {
            // Check and read the file in the JAXB Object
            ESPDResponseType resType = readRegulatedESPDResponseFromStream(bis);
            // Create the Model Object
            res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(resType);
        } catch (IOException ex) {
            Logger.getLogger(OldModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }

        return res;
    }

}
