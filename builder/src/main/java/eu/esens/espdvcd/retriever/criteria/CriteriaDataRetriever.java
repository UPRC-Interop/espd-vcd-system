package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;

/**
 *
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
    List<ECertisCriterion> getNationalCriterionMapping(String ID, String countryCode)
            throws RetrieverException;
        
    /**
     * 
     * @param ID The Criterion ID (European or National).
     * @return Data of Criterion with given ID.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    ECertisCriterion getCriterion(String ID)
            throws RetrieverException;
    
    /**
     * 
     * @param ID The Criterion ID (European or National).
     * @return All Criterion Evidences.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException 
     */
    List<ECertisEvidenceGroup> getEvidences(String ID)
            throws RetrieverException;
                
}
