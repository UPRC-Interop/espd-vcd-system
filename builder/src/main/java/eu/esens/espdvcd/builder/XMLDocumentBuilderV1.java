package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactory;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The XMLDocumentBuilderV1 is a builder pattern implemented class that is used
 * for guided creation of XML Document Artefacts out of ESPD Model Objects.
 * 
 * @since 1.0
 */
public class XMLDocumentBuilderV1 extends DocumentBuilderV1 {

    private ESPDRequest request;

    /**
     * Creates an XMLDocumentBuilder based on {@link ESPDRequest} derived class input
     * 
     * @param req the {@link ESPDRequest} derived class that will be transformed
     * as an XML Document
     * 
     */
    public XMLDocumentBuilderV1(ESPDRequest req) {
        super(req);
        this.request = req;
    }

    /**
     * Transforms the XML Representation of the data to a string.
     * @return the String representation of the XML Data
     */
    public String getAsString() {   
        return theXML;
    }


    @java.lang.Override
    protected String getProfileID() {
        return "ESPD";
    }

    public XMLDocumentBuilderV1 addDefaultESPDCriteriaList() {

        try {
            request.setCriterionList(CriteriaExtractorFactory.getPredefinedESPDCriteriaExtractor(SchemaVersion.V1)
                    .getFullList(request.getFullCriterionList()));
        } catch (RetrieverException ex) {
            Logger.getLogger(XMLDocumentBuilderV1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return XMLDocumentBuilderV1.this;
    }

    public XMLDocumentBuilderV1 addECertisCriteriaList() {

        try {
            request.setCriterionList(CriteriaExtractorFactory.getECertisCriteriaExtractor(SchemaVersion.V1)
                    .getFullList(request.getFullCriterionList()));
        } catch (RetrieverException ex) {
            Logger.getLogger(XMLDocumentBuilderV1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return XMLDocumentBuilderV1.this;
    }

}
