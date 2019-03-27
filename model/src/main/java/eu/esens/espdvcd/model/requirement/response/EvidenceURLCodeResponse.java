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
package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Evidence URL code response
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
// FIXME This class represent's CODE and need generalization
public class EvidenceURLCodeResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Evidence URL code
     * <p>
     * (not specified in domain vocabulary)
     * <p>
     * Data type: <br>
     * Cardinality: <br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
     */
    private String evidenceURLCode;

    public EvidenceURLCodeResponse() {
    }

    public EvidenceURLCodeResponse(String evidenceURLCode) {
        this.evidenceURLCode = evidenceURLCode;
    }

    public String getEvidenceURLCode() {
        return evidenceURLCode;
    }

    public void setEvidenceURLCode(String evidenceURLCode) {
        this.evidenceURLCode = evidenceURLCode;
    }
}
