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
import eu.esens.espdvcd.model.types.ESPDRequestModelType;

import java.io.Serializable;
import java.util.List;

/**
 * This interface is in charge to provide {@link ESPDRequest} data.
 */
public interface ESPDRequest extends Serializable {

    /**
     * A universally unique identifier that can be used to reference
     * this ESPD document instance.
     *
     * In edm version 2 this is mapped to cbc:UUID
     *
     * @return The ID of the {@link ESPDRequest}.
     */
    Long getId();

    /**
     * An identifier for this document, normally generated by the system
     * that creates the ESPD document, or the organisation responsible for
     * the document (e.g. the buyer, e.g. a contracting authority, or the
     * supplier, e.g. an economic operator). An identifier for this
     * document, normally generated by the system that creates the ESPD document,
     * or the organisation responsible for the document (e.g. the buyer, e.g.
     * a contracting authority, or the supplier, e.g. an economic operator).
     * The identifier enables positive referencing the document instance for
     * various purposes including referencing between transactions that are
     * part of the same process.
     *
     * In edm version 2 this is mapped to cbc:ID
     *
     * @return The identifier of this document generally generated by the systems that creates the ESPD
     */
    String getLocalId();

    /**
     * Available model types are in ESPDRequestModelType.
     *
     * @return the model type
     */
    ESPDRequestModelType getModelType();

    /**
     * @return the {@link CADetails} of the ESPD Object
     */
    CADetails getCADetails();

    /**
     * @return the {@link ServiceProviderDetails} of the ESPD Object
     */
    ServiceProviderDetails getServiceProviderDetails();

    /**
     * @return the {@link SelectableCriterion} list of the ESPD Object
     */
    List<SelectableCriterion> getFullCriterionList();

    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that
     * contains all the selection criteria.
     */
    List<SelectableCriterion> getSelectionCriteriaList();

    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that
     * contains all the exclusion criteria.
     */
    List<SelectableCriterion> getExclusionCriteriaList();

    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that
     * contains all the EO Related criteria.
     */
    List<SelectableCriterion> getEORelatedCriteriaList();

    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that
     * contains all the Reduction of candidates selection criteria.
     */
    List<SelectableCriterion> getReductionOfCandidatesCriteriaList();

    /**
     * @param criterionList the {@link SelectableCriterion} list that will be assigned to
     *                      the ESPD Object
     */
    void setCriterionList(List<SelectableCriterion> criterionList);

    /**
     * @param cd The CA Details that will be assigned to the ESPD Object
     */
    void setCADetails(CADetails cd);

    /**
     * @param sp The Service Provider Details that will be assigned to the ESPD Object
     */
    void setServiceProviderDetails(ServiceProviderDetails sp);

    /**
     * @param modelType
     */
    void setModelType(ESPDRequestModelType modelType);

}
