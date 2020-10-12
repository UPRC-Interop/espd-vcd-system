/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.requirement.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Evidence identifier response
 *
 * Response for defining an evidence.
 *
 */
public class EvidenceIdentifierResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1734098673888993636L;

    private String evidenceSuppliedId;

    public EvidenceIdentifierResponse() {

    }

    public EvidenceIdentifierResponse(String evidenceSuppliedId) {
        this.evidenceSuppliedId = evidenceSuppliedId;
    }

    public String getEvidenceSuppliedId() {
        return evidenceSuppliedId;
    }

    public void setEvidenceSuppliedId(String evidenceSuppliedId) {
        this.evidenceSuppliedId = evidenceSuppliedId;
    }

}
