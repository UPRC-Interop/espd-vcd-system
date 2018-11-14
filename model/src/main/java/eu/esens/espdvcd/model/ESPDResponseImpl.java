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
package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO implementation of {@link ESPDResponse}.
 *
 */
public class ESPDResponseImpl extends ESPDRequestImpl implements ESPDResponse {

    private static final long serialVersionUID = -3343982328572347289L;
    
    protected EODetails eoDetails;
    private ESPDRequestDetails espdRequestDetails;
    private List<Evidence> evidenceList;

    public EODetails getEODetails() {
        return eoDetails;
    }

    public void setEODetails(EODetails eoDetails) {
        this.eoDetails = eoDetails;
    }

    @Override
    public List<URL> getExternalDocuments() {
        return null;
    }

    @Override
    public void setESPDRequestDetails(ESPDRequestDetails espdRequestDetails) {
        this.espdRequestDetails = espdRequestDetails;
    }

    @Override
    public ESPDRequestDetails getESPDRequestDetails() {
        return this.espdRequestDetails;
    }

    @Override
    public void setEvidenceList(List<Evidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    @Override
    public List<Evidence> getEvidenceList() {
        if (evidenceList == null) {
            evidenceList = new ArrayList<>();
        }
        return evidenceList;
    }

}
