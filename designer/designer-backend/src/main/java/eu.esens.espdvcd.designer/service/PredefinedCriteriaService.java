package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorFactory;
import eu.esens.espdvcd.retriever.criteria.PredefinedESPDCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.List;

public class PredefinedCriteriaService implements CriteriaService {

    private final CriteriaExtractor predefinedExtractor;

    public PredefinedCriteriaService(SchemaVersion version) {
        predefinedExtractor = CriteriaExtractorFactory.getPredefinedESPDCriteriaExtractor(version);
    }

    @Override
    public List<SelectableCriterion> getCriteria() throws RetrieverException {
        return predefinedExtractor.getFullList();
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException {
        return predefinedExtractor.getFullList(initialList);
    }

    @Override
    public List<SelectableCriterion> getTranslatedCriteria(String lang) {
        throw new UnsupportedOperationException("Translation is not supported for predefined criteria");
    }
}

