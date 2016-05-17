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

    /**
     *
     * @return the {@link CADetails} of the ESPD Object
     */
    public CADetails getCADetails();
    
    /**
     *
     * @return the {@link SelectableCriterion} list of the ESPD Object
     */
    public List<SelectableCriterion> getFullCriterionList(); 
    
    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that 
     * contains all the selection criteria.
     */
    public List<SelectableCriterion> getSelectionCriteriaList();
    
    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that 
     * contains all the exclusion criteria.
     */
    public List<SelectableCriterion> getExclusionCriteriaList();
    
    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that 
     * contains all the EO Related criteria.
     */
    public List<SelectableCriterion> getEORelatedCriteriaList();
    
    /**
     * @return an immutable {@link SelectableCriterion} list of the ESPD Object that 
     * contains all the Reduction of candidates selection criteria.
     */
    public List<SelectableCriterion> getReductionOfCandidatesCriteriaList();
    
    /**
     * 
     * @param criterionList the {@link SelectableCriterion} list that will be assigned to
     * the ESPD Object
     */
    public void setCriterionList(List<SelectableCriterion> criterionList);        
  
    /**
     * 
     * @param cd The CA Details that will be assigned to the ESPD Object 
     */
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
