/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.schematron;

/**
 * Schematron enum used by XML Artefact Validator.
 *
 * @version 1.0.2
 */
public enum SchematronV102 {

    // ESPD Request
    ESPDReqCLAttributeRules("schematron/v1/1.0.2/ESPDRequest", "02-ESPD-CL-attrb-rules"),

    ESPDReqIDAttributeRules("schematron/v1/1.0.2/ESPDRequest", "03-ESPD-ID-attrb-rules"),

    ESPDReqCommonBRRules("schematron/v1/1.0.2/ESPDRequest", "04-ESPD-Common-BR-rules"),

    // ESPD Response;
    ESPDRespCLAttributeRules("schematron/v1/1.0.2/ESPDResponse", "02-ESPD-CL-attrb-rules"),

    ESPDRespIDAttributeRules("schematron/v1/1.0.2/ESPDResponse", "03-ESPD-ID-attrb-rules"),

    ESPDRespCommonBRRules("schematron/v1/1.0.2/ESPDResponse", "04-ESPD-Common-BR-rules"),

    ESPDRespSpecBRRules("schematron/v1/1.0.2/ESPDResponse", "05-ESPD-Spec-BR-rules");

    private String basicPath;
    private String nameWithoutFileExtension;

    SchematronV102(String basicPath, String nameWithoutFileExtension) {
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
