package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.retriever.interfaces.IECertisCriterion;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface CriteriaDataRetriever {

    /**
     *  
     * @param criterionId The Source Criterion Id (European or National).
     * @param countryCode The Country Identification Code according to ISO 3A.
     * @return All National Criteria which mapped with Source Criterion.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<IECertisCriterion> getNationalCriterionMapping(String criterionId, String countryCode)
            throws RetrieverException;
        
    /**
     * 
     * @param criterionId The Criterion Id (European or National).
     * @return Data of Criterion with given Id.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    IECertisCriterion getCriterion(String criterionId)
            throws RetrieverException;
    
    /**
     * 
     * @param criterionId The Criterion Id (European or National).
     * @return All Criterion Evidences.
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException 
     */
    List<IECertisEvidenceGroup> getEvidences(String criterionId)
            throws RetrieverException;
                
}
