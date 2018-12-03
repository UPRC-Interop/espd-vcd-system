package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.designer.util.CriteriaUtil;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.SelfContainedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum SelfContainedCriteriaService implements CriteriaService {
    INSTANCE;

    private final CriteriaExtractor extractor = new SelfContainedCriteriaExtractorBuilder().build();

    public static SelfContainedCriteriaService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<SelectableCriterion> getCriteria() throws RetrieverException {
        extractor.setLang(EULanguageCodeEnum.EN);
        List<SelectableCriterion> criteria = extractor.getFullList();
        CriteriaUtil.generateUUIDs(criteria);
        return CriteriaUtil.markAsSelected(criteria);
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException {
        extractor.setLang(EULanguageCodeEnum.EN);
        return extractor.getFullList(initialList);
    }

    @Override
    public List<SelectableCriterion> getTranslatedCriteria(String lang) throws RetrieverException {
        extractor.setLang(EULanguageCodeEnum.valueOf(lang.toUpperCase()));
        return extractor.getFullList();
    }

    @Override
    public CriteriaType[] getCriteriaFilters() {
        return CriteriaType.values();
    }
}
