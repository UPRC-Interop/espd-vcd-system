/**
 * Copyright 2016-2020 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria.resource.utils;

import com.typesafe.config.ConfigException;
import eu.esens.espdvcd.codelist.enums.internal.PropertyKeyConfigEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.resource.enums.CardinalityEnum;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxonomyDataUtils {

    private static final Logger LOGGER = Logger.getLogger(TaxonomyDataUtils.class.getName());

    public static CardinalityEnum extractCardinality(boolean mandatory, boolean multiple) {

        if (mandatory && !multiple) {
            return CardinalityEnum.ONE;
        }

        if (!mandatory && !multiple) {
            return CardinalityEnum.ZERO_OR_ONE;
        }

        if (!mandatory) {
            return CardinalityEnum.ZERO_TO_MANY;
        }

        return CardinalityEnum.ONE_TO_MANY;
    }

    public static CardinalityEnum extractCardinality(String cardinality) {

        if (cardinality == null) {
            // taxonomy contains blank cardinality values for some
            // QUESTION_GROUP / QUESTION therefore default value has
            // been set to 1
            return CardinalityEnum.ONE;
        }

        switch (cardinality) {

            case "1":
                return CardinalityEnum.ONE;

            case "0..1":
                return CardinalityEnum.ZERO_OR_ONE;

            case "0..n":
                return CardinalityEnum.ZERO_TO_MANY;

            case "1..n":
                return CardinalityEnum.ONE_TO_MANY;

            default:
                LOGGER.log(Level.WARNING, "Warning... Unknown cardinality tag:" + cardinality
                        + " . Cardinality set to ONE");
                return CardinalityEnum.ONE;

        }
    }

    public static void applyTaxonomyData(SelectableCriterion from, SelectableCriterion to) {

        if (from != null && to != null
                && from.getID() != null && to.getID() != null
                && from.getID().equals(to.getID())) {

            // apply the property keys
            to.getPropertyKeyMap().putAll(from.getPropertyKeyMap());

            // apply cardinalities to all root RequirementGroup/s
            to.getRequirementGroups().forEach(toRg -> applyTaxonomyData(
                    from.getRequirementGroups().stream()
                            .filter(fromRg -> toRg.getID().trim().equals(fromRg.getID().trim()))
                            .findFirst().orElse(null) // from
                    , toRg                                  // to
                    , to.getID()
            ));
        } else if (to != null
                && to.getTypeCode() != null
                && to.getTypeCode().equalsIgnoreCase("CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE")) {
            // MEETS THE OBJECTIVE V1 WORKAROUND
            to.getPropertyKeyMap().put("pk2", "espd.part5.title.eo.declares.that");
            to.getPropertyKeyMap().put("pk1", "espd.part5.title.eo.declares.that.main");
            to.getRequirementGroups().forEach(requirementGroup -> applyTaxonomyData(
                    null,
                    requirementGroup,
                    null));
        } else {
            LOGGER.log(Level.SEVERE, "Error... Cardinalities failed been applied at Criterion level:" + to.getID());
        }

    }

    public static void applyTaxonomyData(RequirementGroup from, RequirementGroup to, String criterionId) {

        if (from != null && to != null) {

            // do the same for sub-RequirementGroup/s
            to.getRequirementGroups().forEach(toRg -> applyTaxonomyData(
                    from.getRequirementGroups().stream()
                            .filter(fromRg -> toRg.getID().trim().equals(fromRg.getID().trim()))
                            .findFirst().orElse(null) // from
                    , toRg                                  // to
                    , criterionId));


            // do the same for requirement/s
            to.getRequirements().stream()
                    .filter(TaxonomyDataUtils::isRequirementBasicInfoNotNull)
                    .forEach(toRq -> applyTaxonomyData(
                            from.getRequirements().stream()
                                    .filter(fromRq -> isRequirementBasicInfoNotNull(fromRq)
                                            && toRq.getDescription().replaceAll("\\?", "").trim()
                                            .equalsIgnoreCase(fromRq.getDescription().replaceAll("\\?", "").trim())
                                    )
                                    .findFirst().orElse(null) // from
                            , toRq                                  // to
                    ));
            // cardinalities
            to.setMultiple(from.isMultiple());
            to.setMandatory(from.isMandatory());
            // requirement group type
            if (from.getType() != null) {
                to.setType(from.getType());
            }
        } else if (to != null) {
            try {
                to.getRequirementGroups().forEach(toRg -> applyTaxonomyData(
                        null // from
                        , toRg                                  // to
                        , criterionId));

                to.getRequirements().stream()
//                        .filter(TaxonomyDataUtils::isRequirementBasicInfoNotNull)
                        .filter(rq -> rq.getPropertyKeyMap().isEmpty())
                        .forEach(TaxonomyDataUtils::applyV1PropertyKeysFromConf);
            } catch (ConfigException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        } else {
            LOGGER.log(Level.SEVERE, "Error... Taxonomy data failed been applied at Requirement Group level:"
                    + to.getID() + ", "
                    + to.getType().name()
                    + " at criterion:" + criterionId);
        }
    }

    public static void applyTaxonomyData(Requirement from, Requirement to) {

        if (from != null && to != null) {
            // cardinalities
            to.setMultiple(from.isMultiple());
            to.setMandatory(from.isMandatory());
            // requirement group type
            if (from.getType() != null) {
                to.setType(from.getType());
            }
            // apply related response value artefact
            if (from.getResponseValuesRelatedArtefact() != null) {
                to.setResponseValuesRelatedArtefact(from.getResponseValuesRelatedArtefact());
            }
            // apply the property keys
            to.getPropertyKeyMap().putAll(from.getPropertyKeyMap());
        } else if (to != null) {
            applyV1PropertyKeysFromConf(to);
        } else {
            LOGGER.log(Level.SEVERE, "Error... Taxonomy data failed been applied at Requirement level:"
                    + to.getID() + ", "
                    + to.getType().name() + ", "
                    + to.getResponseDataType());
        }

    }

    public static boolean isRequirementBasicInfoNotNull(Requirement rq) {
        return rq != null
                && rq.getDescription() != null
                && rq.getResponseDataType() != null
                && rq.getType() != null;
    }

    private static void applyV1PropertyKeysFromConf(Requirement rq) {
        // apply v1 property keys from descriptions

        // workaround for reserved characters
        rq.getPropertyKeyMap().put("pk1", PropertyKeyConfigEnum.INSTANCE.getRequirementDescriptionPropertyKey(rq.getDescription()
                .replaceAll("\\?", "")
                .replaceAll(",", "")
                .replaceAll(":", "")
                .replaceAll("\\.", "")
                .trim()));
    }

}
