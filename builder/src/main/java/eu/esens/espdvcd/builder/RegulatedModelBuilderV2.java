package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;

public class RegulatedModelBuilderV2 extends RegulatedModelBuilder {

    @Override
    RegulatedModelBuilder addDefaultESPDCriteriaList() {
        criteriaExtractor = new CriteriaExtractorBuilder().build();
        return this;
    }

}
