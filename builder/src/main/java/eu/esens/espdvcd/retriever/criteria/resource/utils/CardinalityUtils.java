/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource.utils;

import eu.esens.espdvcd.retriever.criteria.resource.enums.CardinalityEnum;

public class CardinalityUtils {

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

}
