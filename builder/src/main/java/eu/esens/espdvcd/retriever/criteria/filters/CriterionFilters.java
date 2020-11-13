package eu.esens.espdvcd.retriever.criteria.filters;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.codelist.enums.CriterionTypeCodeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 13/11/2020.
 */
public class CriterionFilters {

    /**
     * A predicate to check if a Criterion belongs to OTHER.EO Criteria.
     *
     * @return The predicate
     */
    public static Predicate<SelectableCriterion> isOtherEOCriterion() {
        return CriterionFilters::isOtherEOCriterion;
    }

    /**
     * Check if a Criterion belongs to OTHER.EO Criteria.
     *
     * @param sc The Criterion
     * @return
     */
    public static boolean isOtherEOCriterion(@NotNull SelectableCriterion sc) {
        return sc.getTypeCode()
                .contains(CriterionTypeCodeEnum.CRITERION_OTHER_EO.getValue());
    }

    /**
     * A predicate to check if a Criterion is the ALPHA Criterion.
     *
     * @return The predicate
     */
    public static Predicate<SelectableCriterion> isAlphaCriterion() {
        return CriterionFilters::isAlphaCriterion;
    }

    /**
     * Check if a Criterion is the ALPHA Criterion.
     *
     * @param sc The Criterion
     * @return
     */
    public static boolean isAlphaCriterion(@NotNull SelectableCriterion sc) {
        return sc.getTypeCode()
                .contains(CriterionTypeCodeEnum.CRITERION_SELECTION_ALPHA.getValue());
    }

    /**
     * A predicate to check if a Criterion belongs to SELECTION Criteria.
     *
     * @return The predicate
     */
    public static Predicate<SelectableCriterion> isSelectionCriterion() {
        return CriterionFilters::isSelectionCriterion;
    }

    /**
     * Check if a Criterion belongs to SELECTION Criteria.
     *
     * @param sc The Criterion
     * @return
     */
    public static boolean isSelectionCriterion(@NotNull SelectableCriterion sc) {
        return sc.getTypeCode()
                .contains(CriterionTypeCodeEnum.CRITERION_SELECTION.getValue());
    }

    /**
     * A predicate to check if a Criterion belongs to EXCLUSION Criteria.
     *
     * @return The predicate
     */
    public static Predicate<SelectableCriterion> isExclusionCriterion() {
        return CriterionFilters::isExclusionCriterion;
    }

    /**
     * Check if a Criterion belongs to EXCLUSION Criteria.
     *
     * @param sc The Criterion
     * @return
     */
    public static boolean isExclusionCriterion(@NotNull SelectableCriterion sc) {
        return sc.getTypeCode()
                .contains(CriterionTypeCodeEnum.CRITERION_EXCLUSION.getValue());
    }

    /**
     * A predicate to check if a Criterion is provided by e-Certis.
     *
     * @return The predicate
     */
    public static Predicate<SelectableCriterion> isProvidedByECertis() {
        return Predicate.not(isOtherEOCriterion())
                .and(Predicate.not(isAlphaCriterion()));
    }

    /**
     * Check if a Criterion is provided by e-Certis.
     *
     * @param sc The Criterion
     * @return
     */
    public static boolean isProvidedByECertis(@NotNull SelectableCriterion sc) {
        return !isOtherEOCriterion(sc) && !isAlphaCriterion(sc);
    }

    /**
     * Filter given Criterion list by given predicate.
     *
     * @param criterionList The Criterion list
     * @param predicate     The predicate
     * @return The filtered list
     */
    public static List<SelectableCriterion> filterCriteria(@NotNull List<SelectableCriterion> criterionList,
                                                           @NotNull Predicate<SelectableCriterion> predicate) {
        return criterionList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
