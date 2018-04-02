package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

/**
 * The XMLDocumentBuilderV1 is a builder pattern implemented class that is used
 * for guided creation of XML Document Artefacts out of ESPD Model Objects.
 * 
 * @since 1.0
 */
public class XMLDocumentBuilderV1 extends DocumentBuilderV1 {

    /**
     * Creates an XMLDocumentBuilder based on {@link ESPDRequest} derived class input
     * 
     * @param req the {@link ESPDRequest} derived class that will be transformed
     * as an XML Document
     * 
     */
    public XMLDocumentBuilderV1(ESPDRequest req) {
        super(req);
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


}
