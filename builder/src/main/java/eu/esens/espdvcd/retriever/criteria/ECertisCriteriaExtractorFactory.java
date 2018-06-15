package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

import javax.validation.constraints.NotNull;

public class ECertisCriteriaExtractorFactory extends CriteriaExtractorAbstractFactory {

    @Override
    public CriteriaExtractor createCriteriaExtractor(@NotNull SchemaVersion version) {
        handleSchemaVersionError(version);
        return new ECertisCriteriaExtractor(version);
    }

    @Override
    public MultilingualCriteriaExtractor createMultilingualCriteriaExtractor(@NotNull SchemaVersion version) {
        handleSchemaVersionError(version);
        return new ECertisCriteriaExtractor(version);
    }

}
