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

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * This interface is in charge to provide {@link ESPDResponse} data.
 *
 */
public interface ESPDResponse extends ESPDRequest, Serializable {

    /**
     * 
     * @return the EO Details assigned to the ESPD Response Object 
     */
    EODetails getEODetails();
    
    /**
     * 
     * @param eoDetails the {@link EODetails} to be assigned to the ESPD Response
     * Object.
     */
    void setEODetails(EODetails eoDetails);

    /**
     * @return list of {@link URL} of the {@link ESPDResponse}.
     */
    List<URL> getExternalDocuments();

    /**
     * @param espdRequestDetails the (@link ESPDReqeustDetails) linked to this ESPD Response
     */
    void setESPDRequestDetails(ESPDRequestDetails espdRequestDetails);

    /**
     * @return the (@link ESPDRequestDetails) linked to this ESPD Response
     */
    ESPDRequestDetails getESPDRequestDetails();

    void setEvidenceList(List<Evidence> evidenceList);

    List<Evidence> getEvidenceList();

}
