package eu.esens.espdvcd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(value={ "selectionCriteriaList", "exclusionCriteriaList", "eorelatedCriteriaList", "reductionOfCandidatesCriteriaList" })//, allowGetters=true)

/**
 * POJO implementation of {@link ESPDRequest}.
 *
 */


public class RegulatedESPDRequest implements ESPDRequest {

    private static final long serialVersionUID = -8366478868586274094L;

    private Long id;
    private String localId;

    private ESPDRequestModelType modelType = ESPDRequestModelType.ESPD_REQUEST_DRAFT;
    private CADetails caDetails;
    private ServiceProviderDetails serviceProviderDetails;
    private List<SelectableCriterion> criterionList;
    
    private static final String SELECTION_REGEXP = "^CRITERION.SELECTION.+";
    private static final String EXCLUSION_REGEXP = "^CRITERION.EXCLUSION.+";
    private static final String EO_RELATED_REGEXP = "(?!.*MEETS_THE_OBJECTIVE*)^CRITERION.OTHER.EO_DATA.+";
    private static final String REDUCTION_OF_CANDIDATES_REGEXP = "^CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE*";   
        
    public RegulatedESPDRequest() {

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
