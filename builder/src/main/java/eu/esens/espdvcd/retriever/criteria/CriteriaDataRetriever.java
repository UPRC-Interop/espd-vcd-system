/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface CriteriaDataRetriever {
    
    /**
     * 
     * @param euID
     * @param coyntryCode
     * @return 
     */
    List<CriterionType> getNationalCriteria(String euID, String coyntryCode);
    
    /**
     * 
     * @param euID
     * @return 
     */
    List<CriterionType> getCriterion(String euID);
    
    /**
     * 
     * @param euID
     * @return 
     */
    List<RequirementGroup> getEvidences(String euID);
    
}
