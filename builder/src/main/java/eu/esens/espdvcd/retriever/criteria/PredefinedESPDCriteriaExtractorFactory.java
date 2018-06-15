package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

import javax.validation.constraints.NotNull;

public class PredefinedESPDCriteriaExtractorFactory extends CriteriaExtractorAbstractFactory {

    @Override
    public CriteriaExtractor createCriteriaExtractor(@NotNull SchemaVersion version) {
        handleSchemaVersionError(version);
        return new PredefinedESPDCriteriaExtractor(version);
    }

    @Override
    public MultilingualCriteriaExtractor createMultilingualCriteriaExtractor(@NotNull SchemaVersion version) {
        handleSchemaVersionError(version);
        throw new UnsupportedOperationException("Error... Predefined ESPD criteria extractor currently does not support Multilinguality");
    }

}
