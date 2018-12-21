/**
 * Copyright 2016-2018 University of Piraeus Research Center
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

import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.resource.enums.CardinalityEnum;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CardinalityUtils {

    private static final Logger LOGGER = Logger.getLogger(CardinalityUtils.class.getName());

    public static CardinalityEnum extractCardinality(boolean mandatory, boolean multiple) {

        if (mandatory == true && multiple == false) {
            return CardinalityEnum.ONE;
        }

        if (mandatory == false && multiple == false) {
            return CardinalityEnum.ZERO_OR_ONE;
        }

        if (mandatory == false && multiple == true) {
            return CardinalityEnum.ZERO_TO_MANY;
        }

        if (mandatory == true && multiple == true) {
            return CardinalityEnum.ONE_TO_MANY;
        }

        return CardinalityEnum.ONE;
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
                return CardinalityEnum.ONE;

        }
    }

    public static void applyCardinalities(SelectableCriterion from, SelectableCriterion to) {

        if (from != null && to != null
                && from.getID() != null && to.getID() != null
                && from.getID().equals(to.getID())) {

            to.getRequirementGroups().forEach(toRg -> applyCardinalities(
                    from.getRequirementGroups().stream()
                            .filter(fromRg -> toRg.getID().equals(fromRg.getID()))
                            .findFirst().orElse(null) // from
                    , toRg                                  // to
            ));

        } else {
            LOGGER.log(Level.SEVERE, "Error... Cardinalities failed to be applied.");
        }

    }

    private static void applyCardinalities(RequirementGroup from, RequirementGroup to) {

        if (from != null && to != null) {

            to.getRequirementGroups().forEach(toRg -> applyCardinalities(
                    from.getRequirementGroups().stream()
                            .filter(fromRg -> toRg.getID().equals(fromRg.getID()))
                            .findFirst().orElse(null) // from
                    , toRg));                               // to

            to.getRequirements().stream()
                    .filter(toRq -> isRequirementBasicInfoNotNull(toRq))
                    .forEach(toRq -> applyCardinalities(
                            from.getRequirements().stream()
                                    .filter(fromRq -> isRequirementBasicInfoNotNull(fromRq)
                                            && ArtefactUtils.clearAllWhitespaces(toRq.getDescription())
                                            .equals(ArtefactUtils.clearAllWhitespaces(fromRq.getDescription()))
                                            && toRq.getResponseDataType() == fromRq.getResponseDataType()
                                            && toRq.getType() == fromRq.getType())
                                    .findFirst().orElse(null) // from
                            , toRq                                  // to
                    ));

            to.setMultiple(from.isMultiple());
            to.setMandatory(from.isMandatory());
        }
    }

    private static void applyCardinalities(Requirement from, Requirement to) {

        if (from != null && to != null) {
            to.setMultiple(from.isMultiple());
            to.setMandatory(from.isMandatory());
        }

    }

    private static boolean isRequirementBasicInfoNotNull(Requirement rq) {
        return rq != null
                && rq.getDescription() != null
                && rq.getResponseDataType() != null
                && rq.getType() != null;
    }

}
