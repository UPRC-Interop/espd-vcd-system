package eu.esens.espdvcd.builder.util;

/**
 * XSD enum used by ESPD and VCD models.
 *
 */

public enum XSD {
    ESPD_REQUEST("urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1", "espd-req", "xsd/maindoc/ESPDRequest-1.0.xsd"),
    ESPD_RESPONSE("urn:grow:names:specification:ubl:schema:xsd:ESPDResponse-1","espd-res", "xsd/maindoc/ESPDResponse-1.0.xsd"),
    ESPD_CAC("urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonAggregateComponents-1","espd-cac","xsd/common/ESPD-CommonAggregateComponents-1.0.xsd"),
    ESPD_CBC("urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonBasicComponents-1","espd-cbc","xsd/common/ESPD-CommonBasicComponents-1.0.xsd"),
    CCV_CAC("urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1","ccv-cac","xsd/common/CCV-CommonAggregateComponents-1.0.xsd"),
    CCV_CBC("urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1","ccv-cbc","xsd/common/CCV-CommonBasicComponents-1.0.xsd"),
    CEV_CAC("urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1","cev-cac","xsd/common/CEV-CommonAggregateComponents-1.0.xsd"),
    CEV_CBC("urn:isa:names:specification:ubl:schema:xsd:CEV-CommonBasicComponents-1","cev-cbc","xsd/common/CEV-CommonBasicComponents-1.0.xsd"),
    UBL_CAC("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2","ubl-cac","xsd/common/UBL-CommonAggregateComponents-2.1.xsd"),
    UBL_CBC("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2","ubl-cbc","xsd/common/UBL-CommonBasicComponents-2.1.xsd"),
    UBL_EXT("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2","ubl-ext","xsd/common/UBL-CommonExtensionComponents-2.1.xsd"),
    UNCENFACT_CCTS("urn:un:unece:uncefact:documentation:2","ccts","xsd/common/CCTS_CCT_SchemaModule-2.1.xsd"),
    XML_SCHEMA("http://www.w3.org/2001/XMLSchema","xsd","");
    
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


