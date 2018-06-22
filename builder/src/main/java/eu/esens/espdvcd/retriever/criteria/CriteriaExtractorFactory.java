package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

public abstract class CriteriaExtractorFactory {

    public abstract CriteriaExtractor createCriteriaExtractor(SchemaVersion version);

}
