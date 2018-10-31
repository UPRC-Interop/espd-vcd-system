package eu.esens.espdvcd.designer.util;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public final class CriteriaUtil {

    //Prevent Class Instantiation
    private CriteriaUtil() {
    }

    public static boolean hasNullCriterion(@NotNull final List<SelectableCriterion> criteria) {
        return criteria.stream()
                .anyMatch(Objects::isNull);
    }

    public static int satisfiesAllComparator(SelectableCriterion cr1, SelectableCriterion cr2) {
        if (cr1.getTypeCode().equals("CRITERION.SELECTION.ALL_SATISFIED"))
            return -1;
        else if (cr2.getTypeCode().equals("CRITERION.SELECTION.ALL_SATISFIED"))
            return 1;
        else
            return 0;
    }

//    public static List<SelectableCriterion> generateWeightIndicators(@NotNull final List<SelectableCriterion> criteria) {
//        Objects.requireNonNull(criteria);
//        criteria.forEach(selectableCriterion -> generateWeightIndicatorsForRequirementGroups(selectableCriterion.getRequirementGroups()));
//        return criteria;
//    }
//
//    private static void generateWeightIndicatorsForRequirementGroups(@NotNull final List<RequirementGroup> requirementGroups) {
//        requirementGroups.forEach(requirementGroup -> {
//            requirementGroup.getRequirements()
//                    .stream()
//                    .filter(requirement -> requirement.getResponseDataType().equals(ResponseTypeEnum.WEIGHT_INDICATOR))
//                    .findAny()
//                    .ifPresent(requirement -> {
//                        WeightIndicatorResponse response = (WeightIndicatorResponse) requirement.getResponse();
//                        if (Objects.nonNull(response.getEvaluationMethodType()) && response.getEvaluationMethodType().equals("WEIGHTED")) {
////                            response.setIndicator(true);
//                            try {
//                                Field f = FieldUtil.getField(response.getClass(), "indicator");
//                                f.setAccessible(true);
//                                f.setBoolean(response, true);
//                            } catch (NoSuchFieldException e) {
//                                Logger.getLogger(CriteriaUtil.class.getName()).warning("Indicator field not found.");
//                            } catch (IllegalAccessException e) {
//                                Logger.getLogger(CriteriaUtil.class.getName()).warning("Indicator field cannot be set.");
//                            }
//                        }
//                    });
//            generateWeightIndicatorsForRequirementGroups(requirementGroup.getRequirementGroups());
//        });
//    }

    public static List<SelectableCriterion> generateUUIDs(@NotNull final List<SelectableCriterion> criteria) {
        Objects.requireNonNull(criteria);
        int i = 0;
        for (SelectableCriterion criterion : criteria) {
            criterion.setUUID(Integer.toString(i));
            generateUUIDSForRequirementGroups(criterion.getRequirementGroups(), i);
            i++;
        }
        return criteria;
    }

    private static void generateUUIDSForRequirementGroups(@NotNull final List<RequirementGroup> reqGroups, @NotNull int i) {
        for (RequirementGroup reqGroup : reqGroups) {
            i++;
            reqGroup.setUUID(String.format("%s-%d", reqGroup.getID(), i));
            generateUUIDSForRequirementGroups(reqGroup.getRequirementGroups(), i);
            List<Requirement> reqs = reqGroup.getRequirements();
            for (Requirement req : reqs) {
                i++;
                req.setUUID(String.format("%s-%d", req.getID(), i));
            }
        }
    }
}
