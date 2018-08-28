package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface RequirementsResource extends Resource {

    /**
     * Returns the requirements of a particular criterion. The requirements are
     * in a list of requirement groups.
     *
     * @param ID The criterion ID
     * @return
     * @throws RetrieverException
     */
    List<RequirementGroup> getRequirementsForCriterion(String ID) throws RetrieverException;

}
