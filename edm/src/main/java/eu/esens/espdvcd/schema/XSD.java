package eu.esens.espdvcd.schema;

/**
 * XSD enum used by ESPD and VCD models.
 */

public enum XSD {

    // Schema version 1
    ESPD_REQUEST("urn:grow:names:specification:ubl:edm:xsd:ESPDRequest-1", "espd-req", "edm/v1/maindoc/ESPDRequest-1.0.xsd"),
    ESPD_RESPONSE("urn:grow:names:specification:ubl:edm:xsd:ESPDResponse-1", "espd-res", "edm/v1/maindoc/ESPDResponse-1.0.xsd"),
    ESPD_CAC("urn:grow:names:specification:ubl:edm:xsd:ESPD-CommonAggregateComponents-1", "espd-cac", "edm/v1/common/ESPD-CommonAggregateComponents-1.0.xsd"),
    ESPD_CBC("urn:grow:names:specification:ubl:edm:xsd:ESPD-CommonBasicComponents-1", "espd-cbc", "edm/v1/common/ESPD-CommonBasicComponents-1.0.xsd"),
    CCV_CAC("urn:isa:names:specification:ubl:edm:xsd:CCV-CommonAggregateComponents-1", "ccv-cac", "edm/v1/common/CCV-CommonAggregateComponents-1.0.xsd"),
    CCV_CBC("urn:isa:names:specification:ubl:edm:xsd:CCV-CommonBasicComponents-1", "ccv-cbc", "edm/v1/common/CCV-CommonBasicComponents-1.0.xsd"),
    CEV_CAC("urn:isa:names:specification:ubl:edm:xsd:CEV-CommonAggregateComponents-1", "cev-cac", "edm/v1/common/CEV-CommonAggregateComponents-1.0.xsd"),
    CEV_CBC("urn:isa:names:specification:ubl:edm:xsd:CEV-CommonBasicComponents-1", "cev-cbc", "edm/v1/common/CEV-CommonBasicComponents-1.0.xsd"),
    UBL_CAC("urn:oasis:names:specification:ubl:edm:xsd:CommonAggregateComponents-2", "ubl-cac", "edm/v1/common/UBL-CommonAggregateComponents-2.1.xsd"),
    UBL_CBC("urn:oasis:names:specification:ubl:edm:xsd:CommonBasicComponents-2", "ubl-cbc", "edm/v1/common/UBL-CommonBasicComponents-2.1.xsd"),
    UBL_EXT("urn:oasis:names:specification:ubl:edm:xsd:CommonExtensionComponents-2", "ubl-ext", "edm/v1/common/UBL-CommonExtensionComponents-2.1.xsd"),
    UNCENFACT_CCTS("urn:un:unece:uncefact:documentation:2", "ccts", "edm/v1/common/CCTS_CCT_SchemaModule-2.1.xsd"),
    XML_SCHEMA("http://www.w3.org/2001/XMLSchema", "xsd", ""),

    // edm version 2
    ESPD_REQUEST_V2("urn:X-test:UBL:Pre-award:QualificationApplicationRequest", "", "edm/v2/maindoc/UBL-QualificationApplicationRequest-2.2-Pre-award.xsd"),
    ESPD_RESPONSE_V2("urn:X-test:UBL:Pre-award:QualificationApplicationResponse", "", "edm/v2/maindoc/UBL-QualificationApplicationResponse-2.2-Pre-award.xsd"),
    UBL_CAC_PRE_AWARD_V2("urn:X-test:UBL:Pre-award:CommonAggregate", "cac", "edm/v2/common/UBL-CommonAggregateComponents-Pre-award.xsd"),
    UBL_CBC_PRE_AWARD_V2("urn:X-test:UBL:Pre-award:CommonBasic", "cbc", "edm/v2/common/UBL-CommonBasicComponents-Pre-award.xsd"),
    UBL_EXT_PRE_AWARD_V2("urn:oasis:names:specification:ubl:edm:xsd:CommonExtensionComponents-2", "ext", "edm/v2/common/UBL-CommonExtensionComponents-Pre-award.xsd");

    private final String namespaceURI;
    private final String namespacePrefix;
    private final String xsdPath;

    XSD(String namespaceURI, String namespacePrefix, String xsdPath) {
        this.namespaceURI = namespaceURI;
        this.namespacePrefix = namespacePrefix;
        this.xsdPath = xsdPath;
    }

    public String namespaceURI() {
        return namespaceURI;
    }

    public String namespacePrefix() {
        return namespacePrefix;
    }

    public String xsdPath() {
        return xsdPath;
    }

    public static XSD fromString(String text) {
        if (text != null) {
            for (XSD b : XSD.values()) {
                if (text.equalsIgnoreCase(b.namespaceURI)) {
                    return b;
                }
            }
        }
        throw new IllegalArgumentException("Unknown Namespace URI");
    }

}


