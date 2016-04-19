package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import java.io.Serializable;
import java.util.List;

/**
 * This interface is in charge to provide {@link ESPDRequest} data.
 *
 */
public interface ESPDRequest extends Serializable {
    /**
     * @return The ID of the {@link ESPDRequest}.
     */
    
    Long getId();

    /**
     * Available model types are in ESPDRequestModelType.
     *
     * @return the model type
     */
    public ESPDRequestModelType getModelType();

    public CADetails getCADetails();
    
    public List<SelectableCriterion> getFullCriterionList(); 
    
    public List<SelectableCriterion> getSelectionCriteriaList();
    public List<SelectableCriterion> getExclusionCriteriaList();
    public List<SelectableCriterion> getEORelatedCriteriaList();
    public List<SelectableCriterion> getReductionOfCandidatesCriteriaList();
    
    public void setCriterionList(List<SelectableCriterion> criterionList);        
  
    void setCADetails(CADetails cd);

    /**
     * @param modelType
     */
    void setModelType(ESPDRequestModelType modelType);

    /**
     * @return The {@link grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType} of the {@link ESPDRequest}.
     */
    grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType getEspdRequestType();

    /**
     * @param espdRequestType
     */
    void setEspdRequestType(grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType espdRequestType);
}
