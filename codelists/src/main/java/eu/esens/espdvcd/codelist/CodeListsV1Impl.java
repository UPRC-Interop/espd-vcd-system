package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 *
 * @author konstantinos
 */
public enum CodeListsV1Impl implements CodeLists {
    
    /**
     * Activity Type CodeList
     */
    ActivityType("/gc/v1/ActivityTypeCode-CodeList.gc"),
    /**
     * Amount Type CodeList
     */
    AmountType("/gc/v1/AmountTypeCodes-CodeList.gc"),
    /**
     * Contract Type CodeList
     */
    ContractType("/gc/v1/ContractType-CodeList.gc"),
    /**
     * Country Identification according to ISO 3A
     */
    CountryIdentification("/gc/v1/CountryCodeIdentifier-CodeList.gc"),
    /**
     * ESPD Criteria Type taxonomy, flattened as a Codelist
     */
    CriteriaType("/gc/v1/CriteriaTypeCodes-CodeList.gc"),
    /**
     * Criterion Jurisdiction Level for the ESPD Criteria Codelist
     */
    CriterionJurisdictionLevel("/gc/v1/CriterionJurisdictionLevel-CodeList.gc"),
    /**
     * UBL Cusomization ID codelist, used for the ESPD Request and Response
     * Artifacts
     */
    CustomizationID("/gc/v1/CustomizationID-CodeList.gc"),
    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/v1/CurrencyCode-CodeList.gc"),
    /**
     * Document Reference types, used by ESPD Request and Response to add
     * specific external references required by the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/v1/DocRefContentType-CodeList.gc"),
    /**
     * Economic Operator Role Codelist
     */
    EORole("/gc/v1/EORoleCodes-CodeList.gc"),
    /**
     * Language iso code Codelist
     */
    LanguageCodeEU("/gc/v1/LanguageCodeEU-CodeList.gc"),
    /**
     * Response Data type codelist, used to infer the type of response a
     * requirement requires.
     */
    ResponseDataType("/gc/v1/ResponseDataType-CodeList.gc"),
    PeriodMeasureType("/gc/v1/PeriodMeasureTypeCodes-CodeList.gc"),
    PeriodType("/gc/v1/PeriodTypeCodes-CodeList.gc"),
    ProcedureType("/gc/v1/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/v1/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/v1/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/v1/ServicesProjectSubType-CodeList.gc"),
    TechnicalCapabilityType("/gc/v1/TechnicalCapabilityTypeCode-CodeList.gc"),
    TenderingRole("/gc/v1/TendererRole-CodeList.gc");
    
    private final String name;

    private volatile GenericCode INSTANCE;

    private CodeListsV1Impl(String s) {
        name = s;
    }

    private GenericCode getInstance() {
        if (INSTANCE == null) {
            makeInstance();
        }
        return INSTANCE;
    }

    protected synchronized void makeInstance() {
        INSTANCE = new GenericCode(name);
    }
    
    @Override
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }
    
    @Override
    public final String getIdForData(String data) {
        return getInstance().getIdForData(data);
    }
   
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id);
    }

    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value);
    }
    
    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id);
    }
    
    @Override
    public final BiMap<String, String> getBiMap() {
        return getInstance().getBiMap();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getConstantName() {
        return name();
    }

}
