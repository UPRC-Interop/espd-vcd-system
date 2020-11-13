package eu.esens.espdvcd.retriever.criteria.filters;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.codelist.enums.CriterionTypeCodeEnum;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 13/11/2020.
 */
public class CriterionFilters {

    public static Predicate<SelectableCriterion> isProvidedByECertis() {
        return c -> !c.getTypeCode().contains(CriterionTypeCodeEnum.CRITERION_OTHER_EO.getValue())
                && !c.getTypeCode().contains(CriterionTypeCodeEnum.CRITERION_SELECTION_ALPHA.getValue());
    }

    public static boolean isProvidedByECertis(SelectableCriterion sc) {
        return !sc.getTypeCode().contains(CriterionTypeCodeEnum.CRITERION_OTHER_EO.getValue())
                && !sc.getTypeCode().contains(CriterionTypeCodeEnum.CRITERION_SELECTION_ALPHA.getValue());
    }

    public static List<SelectableCriterion> filterCriteria(List<SelectableCriterion> criterionList,
                                                           Predicate<SelectableCriterion> predicate) {
        return criterionList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
