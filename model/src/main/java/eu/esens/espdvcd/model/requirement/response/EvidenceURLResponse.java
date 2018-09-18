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
package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;


/**
 * Evidence URL response
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceURLResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * External document URI
     * <p>
     * The Uniform Resource Identifier (URI) that identifies where the external document is located.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-151<br>
     * BusReqID: tbr92-017, tbr92-022, tbr92-006, tbr92-007<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.
     *                     Evidence.EvidenceDocumentReference.Attachment.ExternalReference.URI<br>
     */
    private String evidenceURL;

    public EvidenceURLResponse() {
    }


    public EvidenceURLResponse(String evidenceURL) {
        this.evidenceURL = evidenceURL;
    }

    public String getEvidenceURL() {
        return evidenceURL;
    }
    public void setEvidenceURL(String evidenceURL) {
        this.evidenceURL = evidenceURL;
    }
}
