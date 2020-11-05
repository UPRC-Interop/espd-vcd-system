package eu.esens.espdvcd.retriever.criteria.resource.utils;

import eu.esens.espdvcd.model.retriever.ECertisCriterion;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 5/11/2020.
 */
public class CriteriaUtils {

    public static boolean isEuropean(ECertisCriterion criterion) {
        return criterion.getLegislationReference().getJurisdictionLevelCode().equals("eu");
    }

}
