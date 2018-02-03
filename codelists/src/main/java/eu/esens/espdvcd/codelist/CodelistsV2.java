package eu.esens.espdvcd.codelist;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Konstantinos Raptis
 */
public enum CodelistsV2 implements MultilingualCodelists {

    /**
     * Bid Type CodeList
     */
    BidType("/gc/v2/BidType-CodeList.gc"),
    /**
     * Set whether an answer to a Criterion Property or/and Evidence can be publicly accessible or not
     */
    ConfidentialityLevel("/gc/v2/ConfidentialityLevel-CodeList.gc"),
    /**
     * Country Identification according to ISO 3A
     */
    CountryIdentification("/gc/v2/CountryCodeIdentifier-CodeList.gc"),
    /**
     * Criterion Element Type CodeList
     */
    CriterionElementType("/gc/v2/CriterionElementType-CodeList.gc"),
    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/v2/CurrencyCode-CodeList.gc"),
    /**
     * Document Reference types, used by ESPD Request and Response to add specific external references required by
     * the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/v2/DocRefContentType-CodeList.gc"),
    /**
     * States the type of identifier of an Economic Operator of a group of tenderers
     */
    EOIDType("/gc/v2/EOIDType-CodeList.gc"),
    /**
     * Economic Operator Industry Classification CodeList
     */
    EOIndustryClassification("/gc/v2/EOIndustryClassificationCode-CodeList.gc"),
    /**
     * Language iso code Codelist
     */
    LanguageCodeEU("/gc/v2/EULanguageCode-CodeList.gc"),
    /**
     * Specifies whether the Criterion is weigthed or a pass/fail one
     */
    EvaluationMethodType("/gc/v2/EvaluationMethodType-CodeList.gc"),
    /**
     * Financial Ratio Type CodeList
     */
    FinancialRatioType("/gc/v2/FinancialRatioType-CodeList.gc"),
    /**
     * Criterion Jurisdiction Level for the ESPD Criteria Codelist
     */
    LegislationType("/gc/v2/LegislationType-CodeList.gc"),
    /**
     * Qualification Application Type, created in order to specify whether the ESPD is "Regulated" or
     * "Self-contained"
     */
    QualificationApplicationType("/gc/v2/QualificationApplicationType-CodeList.gc"),
    /**
     * Response Data type codelist, used to infer the type of response a requirement requires.
     */
    ResponseDataType("/gc/v2/ResponseDataType-CodeList.gc"),
    PeriodMeasureType("/gc/v2/PeriodMeasureType-CodeList.gc"),
    ProcedureType("/gc/v2/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/v2/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/v2/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/v2/ServicesProjectSubType-CodeList.gc"),
    TechnicalCapabilityType("/gc/v2/TechnicalCapabilityType-CodeList.gc"),
    EORoleType("/gc/v2/EORoleType-CodeList.gc"),
    PropertyGroupType("/gc/v2/PropertyGroupType-CodeList.gc");

    private final String name;
    private volatile GenericCode INSTANCE;

    private static final String DEFAULT_LANG = "eng";

    private CodelistsV2(String name) {
        this.name = name;
    }

    private GenericCode getInstance() {
        if (INSTANCE == null) {
            makeInstance();
        }
        return INSTANCE;
    }

    protected synchronized void makeInstance() {
        INSTANCE = new GenericCode(name, DEFAULT_LANG);
    }

    /**
     * @param otherName
     * @return true if the name of the codelist is equal with @param othername
     */
    @Override
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * This method work as wrapper for enum name() method
     *
     * @return the exact name of this ENUM_CONSTANT
     */
    @Override
    public String getConstantName() {
        return name();
    }

    /**
     * @param id
     * @return the value mapped with the specific id in the codelist, null otherwise
     */
    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id, DEFAULT_LANG);
    }

    @Override
    public String getValueForId(String id, String lang) {
        return getInstance().getValueForId(id, lang);
    }

    /**
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id, DEFAULT_LANG);
    }

    /**
     * @param id
     * @param lang
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public boolean containsId(String id, String lang) {
        return getInstance().containsId(id, lang);
    }

    /**
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value, DEFAULT_LANG);
    }

    @Override
    public boolean containsValue(String value, String lang) {
        return getInstance().containsValue(value, lang);
    }

    /**
     * @return the internal representation of the codelist as an immutable map
     */
    @Override
    public final Map<String, String> getDataMap() {
        return getInstance().getDataMap(DEFAULT_LANG);
    }

    /**
     * @param lang The language in which the data will be
     * @return the internal representation of the codelist as an immutable map
     */
    @Override
    public Map<String, String> getDataMap(String lang) {
        return getInstance().getDataMap(lang);
    }

    @Override
    public Optional<Map<String, String>> _getDataMap() {
        return _getDataMap(DEFAULT_LANG);
    }

    @Override
    public Optional<Map<String, String>> _getDataMap(String lang) {
        return getInstance()._getDataMap(lang);
    }

    @Override
    public Optional<String> _getValueForId(String id) {
        return _getValueForId(id, DEFAULT_LANG);
    }

    @Override
    public Optional<String> _getValueForId(String id, String lang) {
        return getInstance()._getValueForId(id, lang);
    }

    /**
     * @return All available languages
     */
    Set<String> getAllLangs() {
        return getInstance().getAllLang();
    }

}
