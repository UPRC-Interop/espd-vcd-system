/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
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
     * @param euCriterionId
     * @param countryCode
     * @return 
     */
    List<CriterionType> getNationalCriterionMapping(String euCriterionId, String countryCode);
    
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
