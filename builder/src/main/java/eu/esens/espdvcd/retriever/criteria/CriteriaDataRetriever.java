package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author Konstantinos Raptis
 */
public interface CriteriaDataRetriever {

    /**
     *  
     * @param ID The Source Criterion ID (European or National).
     * @param countryCode The Country Identification Code according to ISO 2A.
     * @return All National Criteria which mapped with Source Criterion.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getNationalCriterionMapping(String ID, String countryCode) throws RetrieverException;
        
    /**
     * 
     * @param ID The Criterion ID (European or National).
     * @return Data of Criterion with given ID.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    SelectableCriterion getCriterion(String ID) throws RetrieverException;
    
    /**
     * 
     * @param ID The Criterion ID (European or National).
     * @return All Criterion Evidences.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<Evidence> getEvidences(String ID) throws RetrieverException;
                
}
