package eu.esens.espdvcd.codelist;

/**
 *
 * @author konstantinos
 */
public final class CodeListsFactory {

    private CodeListsFactory() {

    }

    /**
     * CodeListsV1 work as a wrapper for codelists version 1.0.2
     *
     */
    public static final class CodeListsV1 {

        public static final CodeLists ACTIVITY_TYPE = CodeListsV1Impl.ActivityType;

        public static final CodeLists AMOUNT_TYPE = CodeListsV1Impl.AmountType;

        public static final CodeLists CONTRACT_TYPE = CodeListsV1Impl.ContractType;

        public static final CodeLists COUNTRY_IDENTIFICATION = CodeListsV1Impl.CountryIdentification;

        public static final CodeLists CRITERIA_TYPE = CodeListsV1Impl.CriteriaType;

        public static final CodeLists CRITERION_JURISDICTION_LEVEL = CodeListsV1Impl.CriterionJurisdictionLevel;

        public static final CodeLists CUSTOMIZATION_ID = CodeListsV1Impl.CustomizationID;

        public static final CodeLists CURRENCY = CodeListsV1Impl.Currency;

        public static final CodeLists DOCUMENT_REFERENCE_CONTENT_TYPE = CodeListsV1Impl.DocumentReferenceContentType;

        public static final CodeLists EO_ROLE = CodeListsV1Impl.EORole;

        public static final CodeLists LANGUAGE_CODE_EU = CodeListsV1Impl.LanguageCodeEU;

        public static final CodeLists RESPONSE_DATA_TYPE = CodeListsV1Impl.ResponseDataType;

        public static final CodeLists PERIOD_MEASURE_TYPE = CodeListsV1Impl.PeriodMeasureType;

        public static final CodeLists PERIOD_TYPE = CodeListsV1Impl.PeriodType;

        public static final CodeLists PROCEDURE_TYPE = CodeListsV1Impl.ProcedureType;

        public static final CodeLists PROFILE_EXECUTION_ID = CodeListsV1Impl.ProfileExecutionID;

        public static final CodeLists PROJECT_TYPE = CodeListsV1Impl.ProjectType;

        public static final CodeLists SERVICES_PROJECT_SUBTREE = CodeListsV1Impl.ServicesProjectSubType;

        public static final CodeLists TECHNICAL_CAPABILITY_TYPE = CodeListsV1Impl.TechnicalCapabilityType;

        public static final CodeLists TENDERING_ROLE = CodeListsV1Impl.TenderingRole;

        public static final CodeLists[] values() {
            return CodeListsV1Impl.values();
        }

        public static final CodeLists valueOf(String name) {
            return CodeListsV1Impl.valueOf(name);
        }

    }

    /**
     * CodeListsV2 work as a wrapper for codelists version 2.0.0
     *
     */
    public static final class CodeListsV2 {

        public static final CodeLists ACTIVITY_TYPE = CodeListsV2Impl.ActivityType;

        public static final CodeLists AMOUNT_TYPE = CodeListsV2Impl.AmountType;

        public static final CodeLists CONFIDENTIAL_LEVEL = CodeListsV2Impl.ConfidentialityLevel;

        public static final CodeLists CONTRACT_TYPE = CodeListsV2Impl.ContractType;

        public static final CodeLists COUNTRY_IDENTIFICATION = CodeListsV2Impl.CountryIdentification;

        public static final CodeLists CRITERIA_TYPE = CodeListsV2Impl.CriteriaType;

        public static final CodeLists CURRENCY = CodeListsV2Impl.Currency;

        public static final CodeLists DOCUMENT_REFERENCE_CONTENT_TYPE = CodeListsV2Impl.DocumentReferenceContentType;

        public static final CodeLists EO_ID_TYPE = CodeListsV2Impl.EOIDType;

        public static final CodeLists LANGUAGE_CODE_EU = CodeListsV2Impl.LanguageCodeEU;

        public static final CodeLists EVALUATION_METHOD_TYPE = CodeListsV2Impl.EvaluationMethodType;

        public static final CodeLists FINANCIAL_RATIO_TYPE = CodeListsV2Impl.FinancialRatioType;

        public static final CodeLists JURISDICTION_LEVEL = CodeListsV2Impl.JurisdictionLevel;

        public static final CodeLists LEGISLATION_TYPE = CodeListsV2Impl.LegislationType;

        public static final CodeLists NUMBER_OF_LOTS_CODE = CodeListsV2Impl.NumberOfLotsCode;

        public static final CodeLists QUALIFICATION_APPLICATION_TYPE = CodeListsV2Impl.QualificationApplicationType;

        public static final CodeLists RESPONSE_DATA_TYPE = CodeListsV2Impl.ResponseDataType;

        public static final CodeLists PERIOD_MEASURE_TYPE = CodeListsV2Impl.PeriodMeasureType;

        public static final CodeLists PROCEDURE_TYPE = CodeListsV2Impl.ProcedureType;

        public static final CodeLists PROFILE_EXECUTION_ID = CodeListsV2Impl.ProfileExecutionID;

        public static final CodeLists PROJECT_TYPE = CodeListsV2Impl.ProjectType;

        public static final CodeLists SERVICES_PROJECT_SUBTYPE = CodeListsV2Impl.ServicesProjectSubType;

        public static final CodeLists TECHNICAL_CAPABILITY_TYPE = CodeListsV2Impl.TechnicalCapabilityType;

        public static final CodeLists EO_ROLE_TYPE = CodeListsV2Impl.EORoleType;

        public static final CodeLists[] values() {
            return CodeListsV2Impl.values();
        }

        public static final CodeLists valueOf(String name) {
            return CodeListsV2Impl.valueOf(name);
        }
    }

}
