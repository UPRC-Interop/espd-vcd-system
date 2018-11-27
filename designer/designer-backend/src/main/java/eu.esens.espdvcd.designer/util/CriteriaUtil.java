package eu.esens.espdvcd.designer.util;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.service.ExportESPDService;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

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

    public static void finalizeESPDResponse(final ESPDResponse document) {
        document.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        document.getFullCriterionList().forEach(cr -> finalizeRequirementGroups(cr.getRequirementGroups()));
    }

    private static void finalizeRequirementGroups(final List<RequirementGroup> requirementGroupList) {
        for (RequirementGroup rg : requirementGroupList) {
            if (rg.getRequirements().get(0).getResponseDataType().equals(ResponseTypeEnum.INDICATOR) && rg.getRequirementGroups().size() > 0) {
                IndicatorResponse indicator = (IndicatorResponse) rg.getRequirements().get(0).getResponse();
                if (indicator != null) {
                    rg.getRequirementGroups().forEach(requirementGroup -> {
                        switch (requirementGroup.getCondition()) {
                            case "ONTRUE":
                                if (!indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null));
                                }
                                break;
                            case "ONFALSE":
                                if (indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null));
                                }
                                break;
                            default:
                                Logger.getLogger(ExportESPDService.class.getName()).warning("Ignoring condition " + requirementGroup.getCondition());
                                break;
                        }
                    });
                }
            }
            finalizeRequirementGroups(rg.getRequirementGroups());
        }
    }

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
