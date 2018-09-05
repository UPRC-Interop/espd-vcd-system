package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;
import java.util.stream.Collectors;

public interface CriteriaService {

    List<SelectableCriterion> getCriteria() throws RetrieverException;

    List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException;

    List<SelectableCriterion> getTranslatedCriteria(String lang) throws RetrieverException;

    CriteriaType[] getCriteriaFilters();

    default List<SelectableCriterion> getFilteredCriteriaList(String criteriaType) throws RetrieverException, IllegalArgumentException {
        return getCriteria().stream()
                .filter(cr -> cr.getTypeCode().matches(CriteriaType.valueOf(criteriaType).getRegex()))
                .collect(Collectors.toList());
    }

    default List<SelectableCriterion> getFilteredTranslatedCriteriaList(String criteriaType, String lang) throws RetrieverException, IllegalArgumentException {
        return getTranslatedCriteria(lang).stream()
                .filter(cr -> cr.getTypeCode().matches(CriteriaType.valueOf(criteriaType).getRegex()))
                .collect(Collectors.toList());
    }
}
