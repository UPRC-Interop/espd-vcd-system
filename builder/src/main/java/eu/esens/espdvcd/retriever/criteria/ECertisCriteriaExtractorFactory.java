package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

public class ECertisCriteriaExtractorFactory extends MultilingualCriteriaExtractorFactory {

    @Override
    public MultilingualCriteriaExtractor createMultilingualCriteriaExtractor(SchemaVersion version) {
        return new ECertisCriteriaExtractor(version);
    }

}
