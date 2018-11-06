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
package eu.esens.espdvcd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.ArtefactType;
import eu.esens.espdvcd.schema.EDMVersion;


import java.io.Serializable;

public class DocumentDetails implements Serializable {

    private static final long serialVersionUID = 5723048808969930122L;
    /**
     * Class that contains information about the ESPD document
     */

    private final EDMVersion version;
    private final ArtefactType artefactType;
    private final QualificationApplicationTypeEnum qualificationApplicationType;

    /**
     * Constructor for the DocumentDetails object
     *
     * @param version                      version of the ESPD (EDMVersion enum: V1, V2)
     * @param type                         type of the ESPD (ArtefactType enum: ESPD_REQUEST, ESPD_RESPONSE)
     * @param qualificationApplicationType whether the ESPD is SELF_CONTAINED or REQULATED
     */
    @JsonCreator
    public DocumentDetails(@JsonProperty("version") EDMVersion version,
                           @JsonProperty("type") ArtefactType type,
                           @JsonProperty("qualificationApplicationType") QualificationApplicationTypeEnum qualificationApplicationType) {
        this.version = version;
        this.artefactType = type;
        this.qualificationApplicationType = qualificationApplicationType;
    }

    public EDMVersion getVersion() {
        return version;
    }

    public ArtefactType getType() {
        return artefactType;
    }

    public QualificationApplicationTypeEnum getQualificationApplicationType() {
        return qualificationApplicationType;
    }
}
