package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.LegislationReference;

public interface LegislationResource {

    LegislationReference getLegislationForCriterion(String ID);

}
