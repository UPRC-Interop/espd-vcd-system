package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.schema.EDMVersion;

public class RegulatedModelBuilderV1 extends RegulatedModelBuilder {

    @Override
    RegulatedModelBuilder addDefaultESPDCriteriaList() {
        criteriaExtractor = new CriteriaExtractorBuilder(EDMVersion.V1).build();
        return this;
    }

}
