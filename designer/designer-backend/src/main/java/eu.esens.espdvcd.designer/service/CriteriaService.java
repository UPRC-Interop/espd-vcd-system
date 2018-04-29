package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface CriteriaService {

    List<SelectableCriterion> getCriteria() throws RetrieverException;

    List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException;

    List<SelectableCriterion> getTranslatedCriteria(String lang) throws RetrieverException;

    default List<SelectableCriterion> getFilteredCriteriaList(String criteriaType) throws RetrieverException, IllegalArgumentException {
        List<SelectableCriterion> crList = getCriteria().stream()
                .filter(cr -> cr.getTypeCode().matches(CriteriaType.valueOf(criteriaType).getRegex()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(crList);
    }

    default List<SelectableCriterion> getFilteredTranslatedCriteriaList(String criteriaType, String lang) throws RetrieverException, IllegalArgumentException {
        List<SelectableCriterion> crList = getTranslatedCriteria(lang).stream()
                .filter(cr -> cr.getTypeCode().matches(CriteriaType.valueOf(criteriaType).getRegex()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(crList);
    }
}
