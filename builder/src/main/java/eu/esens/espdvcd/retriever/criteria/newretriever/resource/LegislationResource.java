package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

/**
 * @author konstantinos Raptis
 */
public interface LegislationResource extends Resource {

    LegislationReference getLegislationForCriterion(String ID) throws RetrieverException;

}
