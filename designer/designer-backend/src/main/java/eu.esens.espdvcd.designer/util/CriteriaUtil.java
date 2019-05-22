/**
 * Copyright 2016-2019 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.designer.util;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.exception.LanguageNotExistsException;
import eu.esens.espdvcd.designer.service.ExportESPDService;
import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class CriteriaUtil {

    //Prevent Class Instantiation
    private CriteriaUtil() {
    }

    public static boolean hasNullCriterion(@NotNull final List<SelectableCriterion> criteria) {
        return criteria.stream()
                .anyMatch(Objects::isNull);
    }

    public static int satisfiesAllComparator(final SelectableCriterion cr1, final SelectableCriterion cr2) {
        if (cr1.getTypeCode().matches(CriteriaType.ALL_SATISFIED.getRegex()))
            return -1;
        else if (cr2.getTypeCode().matches(CriteriaType.ALL_SATISFIED.getRegex()))
            return 1;
        else
            return 0;
    }

    public static int countryListComparator(final CodelistItem item1, final CodelistItem item2) {
        if (item1.getCode().equals(
                AppConfig.getInstance().getDefaultCountry())) {
            return -1;
        } else if (item2.getCode().equals(
                AppConfig.getInstance().getDefaultCountry())) {
            return 1;
        } else
            return 0;
    }

    public static int currencyListComparator(final CodelistItem item1, final CodelistItem item2) {
        if (item1.getCode().equals(
                AppConfig.getInstance().getDefaultCurrency())) {
            return -1;
        } else if (item2.getCode().equals(
                AppConfig.getInstance().getDefaultCurrency())) {
            return 1;
        } else
            return 0;
    }

    public static ESPDRequest removeSelectionCriteriaIfAlpha(final ESPDRequest model) {
        model.getFullCriterionList()
                .stream()
                .filter(cr -> cr
                        .getTypeCode()
                        .matches(CriteriaType.ALL_SATISFIED.getRegex()))
                .findFirst()
                .ifPresent(cr -> {
                    if (cr.isSelected())
                        model.getFullCriterionList()
                                .removeIf(selectableCriterion -> selectableCriterion
                                        .getTypeCode()
                                        .matches(CriteriaType.SELECTION_NO_ALPHA.getRegex()));

                });
        return model;
    }

    public static List<SelectableCriterion> markAsSelected(final List<SelectableCriterion> criteria) {
        return criteria.stream()
                .peek(selectableCriterion -> {
                    if (selectableCriterion.getTypeCode().matches(CriteriaType.SELECTION_NO_ALPHA.getRegex())) {
                        selectableCriterion.setSelected(AppConfig.getInstance().isSelectionCriteriaPreselected());
                    } else if (selectableCriterion.getTypeCode().matches(CriteriaType.EXCLUSION_D.getRegex())) {
                        selectableCriterion.setSelected(AppConfig.getInstance().isPurelyNationalPreselected());
                    } else {
                        selectableCriterion.setSelected(true);
                    }
                })
                .collect(Collectors.toList());
    }

    public static ESPDResponse finalizeESPDResponse(@NotNull final ESPDResponse model) {
        model.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null ||
                e.getEvidenceURL().isEmpty());
        model.getFullCriterionList().forEach(cr -> finalizeRequirementGroups(cr.getRequirementGroups()));
        return model;
    }

    private static void finalizeRequirementGroups(@NotNull final List<RequirementGroup> requirementGroupList) {
        Objects.requireNonNull(requirementGroupList);
        for (RequirementGroup rg : requirementGroupList) {
            if (rg.getRequirements().size() > 0) {
                for (int i = 0; i < rg.getRequirements().size(); i++) {
                    if (rg.getRequirements().get(i).getResponseDataType().equals(ResponseTypeEnum.INDICATOR) && rg.getRequirementGroups().size() > 0) {
                        IndicatorResponse indicator = (IndicatorResponse) rg.getRequirements().get(i).getResponse();
                        if (indicator != null) {
                            rg.getRequirementGroups().forEach(requirementGroup -> {
                                switch (requirementGroup.getCondition()) {
                                    case "ONTRUE":
                                    case "GROUP_FULFILLED.ON_TRUE":
                                        if (!indicator.isIndicator()) {
                                            nullifier(requirementGroup);
                                        }
                                        break;
                                    case "ONFALSE":
                                    case "GROUP_FULFILLED.ON_FALSE":
                                        if (indicator.isIndicator()) {
                                            nullifier(requirementGroup);
                                        }
                                        break;
                                    default:
                                        Logger.getLogger(ExportESPDService.class.getName()).warning("Ignoring condition " + requirementGroup.getCondition());
                                        break;
                                }
                            });
                        } else {
                            //If null, erase everything anyway
                            rg.getRequirementGroups().forEach(requirementGroup -> requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null)));
                        }
                    }
                    finalizeRequirementGroups(rg.getRequirementGroups());
                }
            }
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


    public static String findISO6393LangCode(String ISO6391LangCode) throws LanguageNotExistsException {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().equals(ISO6391LangCode)) {
                return locale.getISO3Language();
            }
        }
        throw new LanguageNotExistsException("Language does not exist.");
    }

    private static void nullifier(RequirementGroup requirementGroup) {
        requirementGroup.getRequirementGroups().forEach(CriteriaUtil::nullifier);
        requirementGroup.getRequirements().forEach(requirement -> requirement.setResponse(null));
    }
}
