package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.exception.RetrieverException;
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
     * @param sourceId The Source Criterion Id (European or National).
     * @param targetCountryCode The Country Identification according to ISO 3A.
     * @return All National Criteria which mapped with Source Criterion Id.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<CriterionType> getNationalCriterionMapping(String sourceId, String targetCountryCode)
            throws RetrieverException;
        
    /**
     * 
     * @param criterionId The Criterion Id (European or National).
     * @return Criterion Data of Criterion with given Id or null if Criterion does not exist.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    CriterionType getCriterion(String criterionId)
            throws RetrieverException;
    
    /**
     * 
     * @param criterionId
     * @return 
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException 
     */
    List<RequirementGroupType> getEvidences(String criterionId)
            throws RetrieverException;
                
}
