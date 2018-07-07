package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.List;

public interface RequirementGroupResource {

    List<RequirementGroup> getRequirementGroupsForCriterion(String ID);

}
