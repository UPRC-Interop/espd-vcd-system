package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.schema.EDMVersion;

public class RegulatedModelBuilderV2 extends RegulatedModelBuilder {

    @Override
    RegulatedModelBuilder addDefaultESPDCriteriaList() {
        criteriaExtractor = new CriteriaExtractorBuilder(EDMVersion.V2).build();
        return this;
    }

}
