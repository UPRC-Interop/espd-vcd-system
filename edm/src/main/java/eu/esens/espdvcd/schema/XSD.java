/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.schema;

import eu.esens.espdvcd.schema.enums.EDMSubVersion;

/**
 * XSD enum used by ESPD and VCD models.
 */

public enum XSD {

    // 1.0.2
    ESPD_REQUEST_V102("urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1", "espd-req", "xsd/v1/1.0.2/maindoc/ESPDRequest-1.0.xsd", EDMSubVersion.V102.getTag()),
    ESPD_RESPONSE_V102("urn:grow:names:specification:ubl:schema:xsd:ESPDResponse-1", "espd-res", "xsd/v1/1.0.2/maindoc/ESPDResponse-1.0.xsd", EDMSubVersion.V102.getTag()),

    ESPD_CAC_10("urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonAggregateComponents-1", "espd-cac", "xsd/v1/common/ESPD-CommonAggregateComponents-1.0.xsd", EDMSubVersion.V102.getTag()),
    ESPD_CBC_10("urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonBasicComponents-1", "espd-cbc", "xsd/v1/common/ESPD-CommonBasicComponents-1.0.xsd", EDMSubVersion.V102.getTag()),
    CCV_CAC_10("urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1", "ccv-cac", "xsd/v1/common/CCV-CommonAggregateComponents-1.0.xsd", EDMSubVersion.V102.getTag()),
    CCV_CBC_10("urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1", "ccv-cbc", "xsd/v1/common/CCV-CommonBasicComponents-1.0.xsd", EDMSubVersion.V102.getTag()),
    CEV_CAC_10("urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1", "cev-cac", "xsd/v1/common/CEV-CommonAggregateComponents-1.0.xsd", EDMSubVersion.V102.getTag()),
    CEV_CBC_10("urn:isa:names:specification:ubl:schema:xsd:CEV-CommonBasicComponents-1", "cev-cbc", "xsd/v1/common/CEV-CommonBasicComponents-1.0.xsd", EDMSubVersion.V102.getTag()),
    UBL_CAC_21("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "ubl-cac", "xsd/v1/common/UBL-CommonAggregateComponents-2.1.xsd", EDMSubVersion.V102.getTag()),
    UBL_CBC_21("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "ubl-cbc", "xsd/v1/common/UBL-CommonBasicComponents-2.1.xsd", EDMSubVersion.V102.getTag()),
    UBL_EXT_21("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "ubl-ext", "xsd/v1/common/UBL-CommonExtensionComponents-2.1.xsd", EDMSubVersion.V102.getTag()),
    UNCENFACT_CCTS_21("urn:un:unece:uncefact:documentation:2", "ccts", "xsd/v1/common/CCTS_CCT_SchemaModule-2.1.xsd", EDMSubVersion.V102.getTag()),
    XML_SCHEMA("http://www.w3.org/2001/XMLSchema", "xsd", "", EDMSubVersion.V102.getTag()),

    // 2.0.2
    ESPD_REQUEST_V202("urn:X-test:UBL:Pre-award:QualificationApplicationRequest", "", "xsd/v2/2.0.2/maindoc/UBL-QualificationApplicationRequest-2.2-Pre-award.xsd", EDMSubVersion.V202.getTag()),
    ESPD_RESPONSE_V202("urn:X-test:UBL:Pre-award:QualificationApplicationResponse", "", "xsd/v2/2.0.2/maindoc/UBL-QualificationApplicationResponse-2.2-Pre-award.xsd", EDMSubVersion.V202.getTag()),

    // 2.1.0
    ESPD_REQUEST_V210("urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationRequest-2", "", "xsd/v2/2.1.0/maindoc/UBL-QualificationApplicationRequest-2.2.xsd", EDMSubVersion.V210.getTag()),
    ESPD_RESPONSE_V210("urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationResponse-2", "", "xsd/v2/2.1.0/maindoc/UBL-QualificationApplicationResponse-2.2.xsd", EDMSubVersion.V210.getTag()),

    UBL_CAC_22("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "cac", "xsd/v2/2.1.0/common/UBL-CommonAggregateComponents-2.2.xsd", EDMSubVersion.V210.getTag()),
    UBL_CBC_22("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc", "xsd/v2/2.1.0/common/UBL-CommonBasicComponents-2.2.xsd", EDMSubVersion.V210.getTag()),
    UBL_EXT_22("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "ext", "xsd/v2/2.1.0/common/UBL-CommonExtensionComponents-2.2.xsd", EDMSubVersion.V210.getTag()),

    UNCENFACT_CCTS_22("urn:un:unece:uncefact:documentation:2", "ccts", "xsd/v2/2.1.0/common/CCTS_CCT_SchemaModule-2.2.xsd", EDMSubVersion.V210.getTag());

    private final String namespaceURI;
    private final String namespacePrefix;
    private final String xsdPath;
    private final String tag; // The version tag

    XSD(String namespaceURI, String namespacePrefix, String xsdPath, String tag) {
        this.namespaceURI = namespaceURI;
        this.namespacePrefix = namespacePrefix;
        this.xsdPath = xsdPath;
        this.tag = tag;
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

    public String tag() {
        return tag;
    }

    /**
     * @param text
     * @param tag  The version tag
     * @return
     */
    public static XSD fromString(String text, String tag) {
        if (text != null) {
            for (XSD b : XSD.values()) {
                if (text.equalsIgnoreCase(b.namespaceURI)
                        && tag.equalsIgnoreCase(b.tag)) {
                    return b;
                }
            }
        }
        throw new IllegalArgumentException("Unknown Namespace URI");
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


