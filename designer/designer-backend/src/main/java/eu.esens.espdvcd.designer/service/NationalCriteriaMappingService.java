package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetrieverBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public enum NationalCriteriaMappingService {
    INSTANCE;

    final CriteriaDataRetriever retriever = new CriteriaDataRetrieverBuilder().build();

    public static NationalCriteriaMappingService getInstance() {
        return INSTANCE;
    }

    public List<SelectableCriterion> getNationalCriteria(String euCriterionID, String euCountryCode) throws RetrieverException {
        retriever.setLang(EULanguageCodeEnum.EN);
        return retriever.getNationalCriterionMapping(euCriterionID, euCountryCode);
    }

    public List<SelectableCriterion> getTranslatedNationalCriteria(String euCriterionID, String euCountryCode, String lang) throws RetrieverException, IllegalArgumentException {
        retriever.setLang(EULanguageCodeEnum.valueOf(lang.toUpperCase()));
        return retriever.getNationalCriterionMapping(euCriterionID, euCountryCode);
    }
}
