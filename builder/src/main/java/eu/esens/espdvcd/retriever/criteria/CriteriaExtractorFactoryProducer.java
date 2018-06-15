package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.enums.FactoryType;

import javax.validation.constraints.NotNull;

public class CriteriaExtractorFactoryProducer {

    public static CriteriaExtractorAbstractFactory getFactory(@NotNull FactoryType type) {

        switch (type) {

            case PREDEFINED:
                return new PredefinedESPDCriteriaExtractorFactory();

            case ECERTIS:
                return new ECertisCriteriaExtractorFactory();

            default:
                throw new IllegalArgumentException("Error... FactoryType: " + type.name() + " does not supported.");

        }

    }

}
