package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetrieverBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum NationalCriteriaMappingServiceImpl implements NationalCriteriaMappingService {
    INSTANCE;

    final CriteriaDataRetriever retriever = new CriteriaDataRetrieverBuilder().build();

    public static NationalCriteriaMappingService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<SelectableCriterion> getNationalCriteria(String euCriterionID, String euCountryCode) throws RetrieverException {
        retriever.setLang(EULanguageCodeEnum.EN);
        return retriever.getNationalCriterionMapping(euCriterionID, euCountryCode);
    }

    @Override
    public List<SelectableCriterion> getTranslatedNationalCriteria(String euCriterionID, String euCountryCode, String lang) throws RetrieverException {
        retriever.setLang(EULanguageCodeEnum.valueOf(euCountryCode.toUpperCase()));
        return retriever.getNationalCriterionMapping(euCriterionID, euCountryCode);
    }
}
