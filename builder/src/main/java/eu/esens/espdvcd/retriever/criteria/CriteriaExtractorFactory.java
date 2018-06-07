package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

import javax.validation.constraints.NotNull;

public class CriteriaExtractorFactory {

    public static CriteriaExtractor getPredefinedESPDCriteriaExtractor(@NotNull SchemaVersion version) {

        if (version == SchemaVersion.UNKNOWN) {
            throw new IllegalArgumentException("Error... Invalid schema version value.");
        }

        return new PredefinedESPDCriteriaExtractor(version);
    }

    public static CriteriaExtractor getECertisCriteriaExtractor(@NotNull SchemaVersion version) {

        if (version == SchemaVersion.UNKNOWN) {
            throw new IllegalArgumentException("Error... Invalid schema version value.");
        }

        return new ECertisCriteriaExtractor(version);
    }

}
