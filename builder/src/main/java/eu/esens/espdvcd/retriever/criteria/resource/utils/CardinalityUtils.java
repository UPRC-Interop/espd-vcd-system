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
