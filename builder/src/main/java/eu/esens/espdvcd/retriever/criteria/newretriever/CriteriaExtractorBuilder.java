package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;

public interface CriteriaExtractorBuilder {

    CriteriaExtractorBuilder withCriteriaResource(CriteriaResource resource);

    CriteriaExtractorBuilder withLegislationResource(LegislationResource resource);

    CriteriaExtractorBuilder withRequirementGroupsResource(RequirementGroupResource resource);

    CriteriaExtractor build();

}
