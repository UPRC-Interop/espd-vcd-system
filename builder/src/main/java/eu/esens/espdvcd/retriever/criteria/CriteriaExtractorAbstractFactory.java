package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

public abstract class CriteriaExtractorAbstractFactory {

    public abstract CriteriaExtractor createCriteriaExtractor(SchemaVersion version);

    public abstract MultilingualCriteriaExtractor createMultilingualCriteriaExtractor(SchemaVersion version);

    protected void handleSchemaVersionError(SchemaVersion version) {

        if (version == SchemaVersion.UNKNOWN) {
            throw new IllegalArgumentException("Error... Invalid schema version value.");
        }
    }

}
