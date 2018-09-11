package eu.esens.espdvcd.schematron;

/**
 * Schematron enum used by XML Artefact Validator.
 *
 * @version 1.0.2
 */
public enum SchematronV1 {

    // ESPD Request
    ESPDReqCLAttributeRules("schematron/v1/ESPDRequest", "02-ESPD-CL-attrb-rules"),

    ESPDReqIDAttributeRules("schematron/v1/ESPDRequest", "03-ESPD-ID-attrb-rules"),

    ESPDReqCommonBRRules("schematron/v1/ESPDRequest", "04-ESPD-Common-BR-rules"),

    // ESPD Response;
    ESPDRespCLAttributeRules("schematron/v1/ESPDResponse", "02-ESPD-CL-attrb-rules"),

    ESPDRespIDAttributeRules("schematron/v1/ESPDResponse", "03-ESPD-ID-attrb-rules"),

    ESPDRespCommonBRRules("schematron/v1/ESPDResponse", "04-ESPD-Common-BR-rules"),

    ESPDRespSpecBRRules("schematron/v1/ESPDResponse", "05-ESPD-Spec-BR-rules");

    private String basicPath;
    private String nameWithoutFileExtension;

    SchematronV1(String basicPath, String nameWithoutFileExtension) {
        this.basicPath = basicPath;
        this.nameWithoutFileExtension = nameWithoutFileExtension;
    }

    /**
     * Return the schematron file relative path.
     *
     * @return
     */
    public String sch() {
        return basicPath + "/sch/" + nameWithoutFileExtension + ".sch";
    }

    /**
     * Return the cached xslt schematron file relative path.
     *
     * @return
     */
    public String xslt() {
        return basicPath + "/xslt/" + nameWithoutFileExtension + ".xslt";
    }

}
