package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactory;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The XMLDocumentBuilder is a builder pattern implemented class that is used
 * for guided creation of XML Document Artefacts out of ESPD Model Objects.
 *
 * @since 2.0.2
 */
public class XMLDocumentBuilderV2 extends DocumentBuilderV2 {

    ESPDRequest request;

    /**
     * Creates an XMLDocumentBuilderV2 based on {@link ESPDRequest} derived class input
     *
     * @param req the {@link ESPDRequest} derived class that will be transformed
     * as an XML Document
     *
     */
    public XMLDocumentBuilderV2(ESPDRequest req) {
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

    /**
     * 41 is the default value according to ESPD-EDM v2.0.1 implementation guide
     *
     * @return
     */
    @Override
    protected String getProfileID() {
        return "4.1";
    } //Changed to the ESPD validator required value

    public XMLDocumentBuilderV2 addDefaultESPDCriteriaList() {

        try {
            request.setCriterionList(CriteriaExtractorFactory.getPredefinedESPDCriteriaExtractor(SchemaVersion.V2)
                    .getFullList(request.getFullCriterionList()));
        } catch (RetrieverException ex) {
            Logger.getLogger(XMLDocumentBuilderV1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return XMLDocumentBuilderV2.this;
    }

    public XMLDocumentBuilderV2 addECertisCriteriaList() {

        try {
            request.setCriterionList(CriteriaExtractorFactory.getECertisCriteriaExtractor(SchemaVersion.V2)
                    .getFullList(request.getFullCriterionList()));
        } catch (RetrieverException ex) {
            Logger.getLogger(XMLDocumentBuilderV1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return XMLDocumentBuilderV2.this;
    }

}
