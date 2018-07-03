package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.schema.SchemaVersion;

public class PredefinedESPDCriteriaExtractorFactory extends CriteriaExtractorFactory {

    @Override
    public CriteriaExtractor createCriteriaExtractor(SchemaVersion version) {
        return new PredefinedESPDCriteriaExtractor(version);
    }

}
