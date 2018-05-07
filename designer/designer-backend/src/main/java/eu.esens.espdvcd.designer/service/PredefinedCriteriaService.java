package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.PredefinedESPDCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public class PredefinedCriteriaService implements CriteriaService {

    private final CriteriaExtractor predefinedExtractor;

    public PredefinedCriteriaService() {
        predefinedExtractor = new PredefinedESPDCriteriaExtractor();
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
