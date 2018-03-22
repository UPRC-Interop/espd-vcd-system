package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;


public interface CriteriaExtractor {
    
    /**
     * 
     * @return The full criteria list with all the criteria unselected
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     */
    List<SelectableCriterion> getFullList()
            throws RetrieverException;
    
    /**
     *
     * @return the full criteria list with the criteria in the initialList as not selected
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     * 
     */
    List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList)
            throws RetrieverException; 
    
     /**
     *
     * @param initialList if @isSelected is true, the criteria from the @initialList will be 
     * included as selected, otherwise they will be included as not selected 
     * @return the full criteria list with the criteria in the initialList as selected
     * @throws eu.esens.espdvcd.retriever.exception.RetrieverException
     * 
     */
     List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected)
            throws RetrieverException;
}
