package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactory;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactoryProducer;
import eu.esens.espdvcd.retriever.criteria.MultilingualCriteriaExtractorFactory;
import eu.esens.espdvcd.retriever.criteria.enums.FactoryType;
import eu.esens.espdvcd.retriever.criteria.enums.MultilingualFactoryType;
import eu.esens.espdvcd.schema.SchemaVersion;

public class RegulatedModelBuilderV2 extends RegulatedModelBuilder {

    private static final SchemaVersion VERSION = SchemaVersion.V2;

    @Override
    RegulatedModelBuilder addDefaultESPDCriteriaList() {
        CriteriaExtractorFactory f = CriteriaExtractorFactoryProducer.getFactory(FactoryType.PREDEFINED_ESPD);
        criteriaExtractor = f.createCriteriaExtractor(VERSION);
        return this;
    }

    @Override
    RegulatedModelBuilder addECertisESPDCriteriaList() {
        MultilingualCriteriaExtractorFactory f = CriteriaExtractorFactoryProducer.getMultilingualFactory(MultilingualFactoryType.ECERTIS);
        criteriaExtractor = f.createMultilingualCriteriaExtractor(VERSION);
        return this;
    }

}
