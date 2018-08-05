package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public interface EvidencesResource extends Resource {

    List<Evidence> getEvidencesForCriterion(String ID) throws RetrieverException;

}
