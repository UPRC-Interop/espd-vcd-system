package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorAbstractFactory;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactoryProducer;
import eu.esens.espdvcd.retriever.criteria.enums.FactoryType;
import eu.esens.espdvcd.schema.SchemaVersion;

public class RegulatedModelBuilderV2 extends RegulatedModelBuilder {

    private static final SchemaVersion VERSION = SchemaVersion.V2;

    @Override
    RegulatedModelBuilder addDefaultESPDCriteriaList() {
        CriteriaExtractorAbstractFactory af = CriteriaExtractorFactoryProducer.getFactory(FactoryType.PREDEFINED);
        criteriaExtractor = af.createCriteriaExtractor(VERSION);
        return this;
    }

    @Override
    RegulatedModelBuilder addECertisESPDCriteriaList() {
        CriteriaExtractorAbstractFactory af = CriteriaExtractorFactoryProducer.getFactory(FactoryType.ECERTIS);
        criteriaExtractor = af.createCriteriaExtractor(VERSION);
        return this;
    }

}
