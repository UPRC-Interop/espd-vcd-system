package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.schema.SchemaVersion;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelBuilderV2 implements ModelBuilder {

    private EODetails eoDetails = null;
    private CADetails caDetails = null;
    private ServiceProviderDetails serviceProviderDetails = null;
    private CriteriaExtractor criteriaExtractor = null;
    private InputStream importStream = null;

    /* package private constructor. Create only through factory */
    ModelBuilderV2() {
    }

    public ModelBuilderV2 importFrom(InputStream is) {
        importStream = getBufferedInputStream(is);
        return this;
    }

    public ModelBuilderV2 withCADetailsFrom(InputStream is) throws BuilderException {
        // code goes here
        return this;
    }

    public ESPDRequest createRegulatedESPDRequest() throws BuilderException {
        ESPDRequest req;
        if (importStream != null) {
            req = createRegulatedESPDRequestFromXML(importStream);
            if (criteriaExtractor != null) {
                try {
                    req.setCriterionList(criteriaExtractor.getFullList(req.getFullCriterionList()));
                } catch (RetrieverException ex) {
                    Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            req = new RegulatedESPDRequest();
            if (criteriaExtractor != null) {
                try {
                    req.setCriterionList(criteriaExtractor.getFullList());
                } catch (RetrieverException ex) {
                    Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                req.setCriterionList(getEmptyCriteriaList());
            }
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
        // req.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        req.getFullCriterionList().forEach(sc -> applyCriteriaWorkaround(sc, SchemaVersion.V2));

        return req;
    }

    public ESPDRequest createSelfContainedESPDRequest() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDResponse createRegulatedESPDResponse() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDResponse createSelfContainedESPDResponse() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses the input stream and creates an ESPDRequest model instance.
     *
     * @param xmlESPD The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDRequest Model
     *                          fails
     */
    private ESPDRequest createRegulatedESPDRequestFromXML(InputStream xmlESPD) throws BuilderException {

        ESPDRequest req;

        try (InputStream bis = getBufferedInputStream(xmlESPD)) {
            // Check and read the file in the JAXB Object
            QualificationApplicationRequestType reqType = readRegulatedESPDRequestFromStream(bis);
            // Create the Model Object
            req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);

            return req;

        } catch (IOException ex) {
            Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream", ex);
        }

    }

    private QualificationApplicationRequestType readRegulatedESPDRequestFromStream(InputStream is) {
        try {
            // Start with the convenience methods provided by JAXB. If there are
            // performance issues we will switch back to the JAXB API Usage
            return SchemaUtil.V2.getUnmarshaller().unmarshal(new StreamSource(is), QualificationApplicationRequestType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
