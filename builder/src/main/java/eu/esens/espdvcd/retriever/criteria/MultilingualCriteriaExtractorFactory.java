package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

public abstract class MultilingualCriteriaExtractorFactory {

    public abstract MultilingualCriteriaExtractor createMultilingualCriteriaExtractor(SchemaVersion version);

}
