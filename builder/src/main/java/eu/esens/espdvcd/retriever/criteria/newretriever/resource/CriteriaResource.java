package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.SelectableCriterion;

import java.util.List;
import java.util.Map;

/**
 * @author konstantinos Raptis
 */
public interface CriteriaResource {

    List<SelectableCriterion> getCriterionList();

    /**
     * A Map that contains all criteriaResource criteria as values and use their IDs as keys
     *
     * @return
     */
    Map<String, SelectableCriterion> getCriterionMap();

}
