package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

/**
 * The XMLDocumentBuilder is a builder pattern implemented class that is used
 * for guided creation of XML Document Artefacts out of ESPD Model Objects.
 *
 * @since 2.0.2
 */
public class XMLDocumentBuilderV2 extends DocumentBuilderV2 {

    /**
     * Creates an XMLDocumentBuilderV2 based on {@link ESPDRequest} derived class input
     *
     * @param req the {@link ESPDRequest} derived class that will be transformed
     * as an XML Document
     *
     */
    public XMLDocumentBuilderV2(ESPDRequest req) {
        super(req);
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

}
