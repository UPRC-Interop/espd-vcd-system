package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface RequirementGroupResource extends Resource {

    List<RequirementGroup> getRequirementGroupsForCriterion(String ID) throws RetrieverException;

}
