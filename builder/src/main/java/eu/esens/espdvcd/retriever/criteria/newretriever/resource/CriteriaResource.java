package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.SelectableCriterion;

import java.util.List;
import java.util.Map;

/**
 * @author konstantinos Raptis
 */
public interface CriteriaResource extends Resource {

    List<SelectableCriterion> getCriterionList();

}
