package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.LegislationReference;

/**
 * @author konstantinos Raptis
 */
public interface LegislationResource {

    LegislationReference getLegislationForCriterion(String ID);

}
