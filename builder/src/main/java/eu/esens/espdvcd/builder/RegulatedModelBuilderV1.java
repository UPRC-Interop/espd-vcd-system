package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.ESPDArtefactResource;
import eu.esens.espdvcd.schema.SchemaVersion;

public class RegulatedModelBuilderV1 extends RegulatedModelBuilder {

    @Override
    RegulatedModelBuilder addDefaultESPDCriteriaList() {
        ESPDArtefactResource r = new ESPDArtefactResource(SchemaVersion.V1);
        criteriaExtractor = new CriteriaExtractorBuilder()
                .addCriteriaResource(r)
                .addLegislationResource(r)
                .addRequirementGroupsResource(r)
                .build();
        return this;
    }

}
