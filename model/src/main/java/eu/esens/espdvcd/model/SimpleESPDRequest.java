package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * POJO implementation of {@link ESPDRequest}.
 *
 */
public class SimpleESPDRequest implements ESPDRequest {

    private static final long serialVersionUID = -8366478868586274094L;

    private Long id;

    private ESPDRequestModelType modelType = ESPDRequestModelType.ESPD_REQUEST_DRAFT;
    private ESPDRequestType espdRequestType;
    private CADetails caDetails;
    private List<SelectableCriterion> criterionList;
    
    private static final String SELECTION_REGEXP = "^SELECTION.+";
    private static final String EXCLUSION_REGEXP = "^EXCLUSION.+";
    private static final String EO_RELATED_REGEXP = "^DATA_ON_ECONOMIC_OPERATOR*";
    private static final String REDUCTION_OF_CANDIDATES_REGEXP = "^REDUCTION_OF_CANDIDATES*";
    
        

    public SimpleESPDRequest() {
        this.espdRequestType = new ESPDRequestType();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public CADetails getCADetails() {
       return this.caDetails;
    }

    @Override
    public void setCADetails(CADetails cd) {
        this.caDetails = cd;
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
    public ESPDRequestType getEspdRequestType() {
        return espdRequestType;
    }

    @Override
    public void setEspdRequestType(ESPDRequestType espdRequestType) {
        this.espdRequestType = espdRequestType;
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
