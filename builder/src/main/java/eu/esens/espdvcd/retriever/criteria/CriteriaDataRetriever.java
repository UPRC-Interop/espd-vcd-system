package eu.esens.espdvcd.retriever.criteria;

import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface CriteriaDataRetriever {

    /**
     * 
     * @param sourceCriterionId
     * @param targetCountryCode
     * @return 
     */
    List<CriterionType> getNationalCriterionMapping(String sourceCriterionId, String targetCountryCode);
            
    
    /**
     * 
     * @param criterionId
     * @return 
     */
    CriterionType getCriterion(String criterionId);
    
    /**
     * 
     * @param criterionId
     * @return 
     */
    List<RequirementGroupType> getEvidences(String criterionId);
                
}
