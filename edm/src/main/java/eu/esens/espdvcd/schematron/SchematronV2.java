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
package eu.esens.espdvcd.schematron;

/**
 * Schematron enum used by XML Artefact Validator.
 *
 * @version 2.0.2
 */
public enum SchematronV2 {

    // common
    ESPDCodelistsValues("schematron/v2/common", "01-ESPD-codelist-values"),

    ESPDCommonCLAttributes("schematron/v2/common", "01-ESPD-Common-CL-Attributes"),

    ESPDCommonCriterionBR("schematron/v2/common", "03-ESPD-Common-Criterion-BR"),

    ESPDCommonOtherBR("schematron/v2/common", "04-ESPD-Common-Other-BR"),

    // ESPD Request
    ESPDReqCardinality("schematron/v2/ESPDRequest-2.0.2", "02-ESPD-Req-Cardinality-BR"),

    ESPDReqCriterionBR("schematron/v2/ESPDRequest-2.0.2", "03-ESPD-Req-Criterion-BR"),

    ESPDReqOtherBR("schematron/v2/ESPDRequest-2.0.2", "04-ESPD-Req-Other-BR"),

    ESPDReqProcurerBR("schematron/v2/ESPDRequest-2.0.2", "05-ESPD-Req-Procurer-BR"),

    ESPDReqSelfContained("schematron/v2/ESPDRequest-2.0.2", "05-ESPD-Req-Self-contained-BR"),

    // ESPD Response
    ESPDRespCardinalityBR("schematron/v2/ESPDResponse-2.0.2", "02-ESPD-Resp-Cardinality-BR"),

    ESPDRespCriterionBR("schematron/v2/ESPDResponse-2.0.2", "03-ESPD-Resp-Criterion-BR"),

    ESPDRespOtherBR("schematron/v2/ESPDResponse-2.0.2", "04-ESPD-Resp-Other-BR"),

    ESPDRespEOBR("schematron/v2/ESPDResponse-2.0.2", "05-ESPD-Resp-EO-BR"),

    ESPDRespQualificationBR("schematron/v2/ESPDResponse-2.0.2", "05-ESPD-Resp-Qualification-BR"),

    ESPDRespRoleBR("schematron/v2/ESPDResponse-2.0.2", "05-ESPD-Resp-Role-BR"),

    ESPDRespSelfContainedBR("schematron/v2/ESPDResponse-2.0.2", "05-ESPD-Resp-Self-contained-BR");

    private String basicPath;
    private String nameWithoutFileExtension;

    SchematronV2(String basicPath, String nameWithoutFileExtension) {
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
