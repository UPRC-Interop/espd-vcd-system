package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.enums.FactoryType;
import eu.esens.espdvcd.retriever.criteria.enums.MultilingualFactoryType;

import javax.validation.constraints.NotNull;

public class CriteriaExtractorFactoryProducer {

    public static CriteriaExtractorFactory getFactory(@NotNull FactoryType type) {

        switch (type) {

            case PREDEFINED_ESPD:
                return new PredefinedESPDCriteriaExtractorFactory();

            case PREDEFINED_EXCEL:
                return new PredefinedExcelCriteriaExtractorFactory();

            default:
                throw new IllegalArgumentException("Error... FactoryType: " + type.name() + " does not supported.");

        }

    }

    public static MultilingualCriteriaExtractorFactory getMultilingualFactory(@NotNull MultilingualFactoryType type) {

        switch (type) {

            case ECERTIS:
                return new ECertisCriteriaExtractorFactory();

            default:
                throw new IllegalArgumentException("Error... Multilingual FactoryType: " + type.name() + " does not supported.");

        }

    }

}
