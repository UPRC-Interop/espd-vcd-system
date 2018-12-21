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

    // 1.0.2
    ESPD_REQUEST_V102("urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1", "espd-req", "xsd/v1/1.0.2/maindoc/ESPDRequest-1.0.xsd"),
    ESPD_RESPONSE_V102("urn:grow:names:specification:ubl:schema:xsd:ESPDResponse-1", "espd-res", "xsd/v1/1.0.2/maindoc/ESPDResponse-1.0.xsd"),

    // 2.0.2
    ESPD_REQUEST_V202("urn:X-test:UBL:Pre-award:QualificationApplicationRequest", "", "xsd/v2/2.0.2/maindoc/UBL-QualificationApplicationRequest-2.2-Pre-award.xsd"),
    ESPD_RESPONSE_V202("urn:X-test:UBL:Pre-award:QualificationApplicationResponse", "", "xsd/v2/2.0.2/maindoc/UBL-QualificationApplicationResponse-2.2-Pre-award.xsd"),

    // 2.1.0
    ESPD_REQUEST_V210("urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationRequest-2", "", "xsd/v2/2.1.0/maindoc/UBL-QualificationApplicationRequest-2.2.xsd"),
    ESPD_RESPONSE_V210("urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationResponse-2", "", "xsd/v2/2.1.0/maindoc/UBL-QualificationApplicationResponse-2.2.xsd");

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


