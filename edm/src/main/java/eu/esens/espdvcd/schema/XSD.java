/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.schema;

/**
 * XSD enum used by ESPD and VCD models.
 */

public enum XSD {

    // Schema version 1
    ESPD_REQUEST("urn:grow:names:specification:ubl:xsd:xsd:ESPDRequest-1", "espd-req", "xsd/v1/maindoc/ESPDRequest-1.0.xsd"),
    ESPD_RESPONSE("urn:grow:names:specification:ubl:xsd:xsd:ESPDResponse-1", "espd-res", "xsd/v1/maindoc/ESPDResponse-1.0.xsd"),
    ESPD_CAC("urn:grow:names:specification:ubl:xsd:xsd:ESPD-CommonAggregateComponents-1", "espd-cac", "xsd/v1/common/ESPD-CommonAggregateComponents-1.0.xsd"),
    ESPD_CBC("urn:grow:names:specification:ubl:xsd:xsd:ESPD-CommonBasicComponents-1", "espd-cbc", "xsd/v1/common/ESPD-CommonBasicComponents-1.0.xsd"),
    CCV_CAC("urn:isa:names:specification:ubl:xsd:xsd:CCV-CommonAggregateComponents-1", "ccv-cac", "xsd/v1/common/CCV-CommonAggregateComponents-1.0.xsd"),
    CCV_CBC("urn:isa:names:specification:ubl:xsd:xsd:CCV-CommonBasicComponents-1", "ccv-cbc", "xsd/v1/common/CCV-CommonBasicComponents-1.0.xsd"),
    CEV_CAC("urn:isa:names:specification:ubl:xsd:xsd:CEV-CommonAggregateComponents-1", "cev-cac", "xsd/v1/common/CEV-CommonAggregateComponents-1.0.xsd"),
    CEV_CBC("urn:isa:names:specification:ubl:xsd:xsd:CEV-CommonBasicComponents-1", "cev-cbc", "xsd/v1/common/CEV-CommonBasicComponents-1.0.xsd"),
    UBL_CAC("urn:oasis:names:specification:ubl:xsd:xsd:CommonAggregateComponents-2", "ubl-cac", "xsd/v1/common/UBL-CommonAggregateComponents-2.1.xsd"),
    UBL_CBC("urn:oasis:names:specification:ubl:xsd:xsd:CommonBasicComponents-2", "ubl-cbc", "xsd/v1/common/UBL-CommonBasicComponents-2.1.xsd"),
    UBL_EXT("urn:oasis:names:specification:ubl:xsd:xsd:CommonExtensionComponents-2", "ubl-ext", "xsd/v1/common/UBL-CommonExtensionComponents-2.1.xsd"),
    UNCENFACT_CCTS("urn:un:unece:uncefact:documentation:2", "ccts", "xsd/v1/common/CCTS_CCT_SchemaModule-2.1.xsd"),
    XML_SCHEMA("http://www.w3.org/2001/XMLSchema", "xsd", ""),

    // xsd version 2
    ESPD_REQUEST_V2("urn:X-test:UBL:Pre-award:QualificationApplicationRequest", "", "xsd/v2/maindoc/UBL-QualificationApplicationRequest-2.2.xsd"),
    ESPD_RESPONSE_V2("urn:X-test:UBL:Pre-award:QualificationApplicationResponse", "", "xsd/v2/maindoc/UBL-QualificationApplicationResponse-2.2.xsd"),
    UBL_CAC_PRE_AWARD_V2("urn:X-test:UBL:Pre-award:CommonAggregate", "cac", "xsd/v2/common/UBL-CommonAggregateComponents-Pre-award.xsd"),
    UBL_CBC_PRE_AWARD_V2("urn:X-test:UBL:Pre-award:CommonBasic", "cbc", "xsd/v2/common/UBL-CommonBasicComponents-Pre-award.xsd"),
    UBL_EXT_PRE_AWARD_V2("urn:oasis:names:specification:ubl:xsd:xsd:CommonExtensionComponents-2", "ext", "xsd/v2/common/UBL-CommonExtensionComponents-Pre-award.xsd");

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


