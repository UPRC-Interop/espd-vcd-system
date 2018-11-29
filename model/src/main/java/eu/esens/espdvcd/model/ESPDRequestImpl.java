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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.esens.espdvcd.model.types.ESPDRequestModelType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(value={ "selectionCriteriaList", "exclusionCriteriaList", "eorelatedCriteriaList", "reductionOfCandidatesCriteriaList" })//, allowGetters=true)

/**
 * POJO implementation of {@link ESPDRequest}.
 *
 */


public class ESPDRequestImpl implements ESPDRequest {

    private static final long serialVersionUID = -8366478868586274094L;

    private Long id;
    private String localId;

    private ESPDRequestModelType modelType = ESPDRequestModelType.ESPD_REQUEST_DRAFT;
    private CADetails caDetails;
    private ServiceProviderDetails serviceProviderDetails;
    private List<SelectableCriterion> criterionList;
    private DocumentDetails documentDetails;
    
    private static final String SELECTION_REGEXP = "^CRITERION.SELECTION.+";
    private static final String EXCLUSION_REGEXP = "^CRITERION.EXCLUSION.+";
    private static final String EO_RELATED_REGEXP = "(?!.*MEETS_THE_OBJECTIVE*)^CRITERION.OTHER.EO_DATA.+";
    private static final String REDUCTION_OF_CANDIDATES_REGEXP = "^CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE*";   
        
    public ESPDRequestImpl() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getLocalId() {
        return localId;
    }

    @Override
    public CADetails getCADetails() {
       return this.caDetails;
    }

    @Override
    public void setCADetails(CADetails cd) {
        this.caDetails = cd;
    }

    @java.lang.Override
    public ServiceProviderDetails getServiceProviderDetails() {
        return serviceProviderDetails;
    }

    @java.lang.Override
    public void setServiceProviderDetails(ServiceProviderDetails serviceProviderDetails) {
        this.serviceProviderDetails = serviceProviderDetails;
    }

    /** Not used at the moment */
    @Override
    public ESPDRequestModelType getModelType() {
        return modelType;
    }

    @Override
    public void setModelType(ESPDRequestModelType modelType) {
        this.modelType = modelType;
    }

    @Override
    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    @Override
    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }

    @Override
    public List<SelectableCriterion> getFullCriterionList() {
        if (criterionList == null) {
            criterionList = new ArrayList<>();
        }
        return criterionList;
    }

    @Override
    public void setCriterionList(List<SelectableCriterion> criterionList) {
        this.criterionList = criterionList;
    }

    @Override
    public List<SelectableCriterion> getSelectionCriteriaList() {
        return getFilteredCriteriaList(SELECTION_REGEXP);
    }


    @Override
    public List<SelectableCriterion> getExclusionCriteriaList() {
         return getFilteredCriteriaList(EXCLUSION_REGEXP);
    }

    @Override
    public List<SelectableCriterion> getEORelatedCriteriaList() {
         return getFilteredCriteriaList(EO_RELATED_REGEXP);
    }
    
    protected List<SelectableCriterion> getFilteredCriteriaList(String filter) {
       List<SelectableCriterion> crList = criterionList.stream()
               .filter(cr -> cr.getTypeCode().matches(filter))
               .collect(Collectors.toList());
       return Collections.unmodifiableList(crList);
    }

    @Override
    public List<SelectableCriterion> getReductionOfCandidatesCriteriaList() {
        return getFilteredCriteriaList(REDUCTION_OF_CANDIDATES_REGEXP);
    }
}
