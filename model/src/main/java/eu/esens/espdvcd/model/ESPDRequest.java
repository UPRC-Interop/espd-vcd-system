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
    ESPDRequestModelType getModelType();

    /**
     *
     * @return the {@link CADetails} of the ESPD Object
     */
    CADetails getCADetails();

    /**
     *
     * @return the {@link ServiceProviderDetails} of the ESPD Object
     */
    ServiceProviderDetails getServiceProviderDetails();
    
    /**
     *
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
     * 
     * @param criterionList the {@link SelectableCriterion} list that will be assigned to
     * the ESPD Object
     */
    void setCriterionList(List<SelectableCriterion> criterionList);
  
    /**
     * 
     * @param cd The CA Details that will be assigned to the ESPD Object 
     */
    void setCADetails(CADetails cd);

    /**
     *
     * @param sp The Service Provider Details that will be assigned to the ESPD Object
     */
    void setServiceProviderDetails(ServiceProviderDetails sp);

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
