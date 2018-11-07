package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;

public interface NationalCriteriaMappingService {
    List<SelectableCriterion> getNationalCriteria(String euCriterionID, String euCountryCode) throws RetrieverException;

    List<SelectableCriterion> getTranslatedNationalCriteria(String euCriterionID, String euCountryCode, String lang) throws RetrieverException;
}
