package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */

public interface CriteriaExtractor {
    
    /**
     * 
     * @return The full criteria list with all the criteria unselected
     */
    public List<SelectableCriterion> getFullList();
    
    /**
     *
     * @param initialList if @isSelected is true, the criteria from the @initialList will be 
     * included as selected, otherwise they will be included as not selected 
     * @return the full criteria list with the criteria in the initialList as selected
     */
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList); 
    
}
