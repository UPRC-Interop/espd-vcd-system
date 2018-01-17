package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;
import java.util.Set;

/**
 *
 * @author Konstantinos Raptis
 */
public enum MultilingualCodeList implements CodeList {

    /**
     * Activity Type CodeList
     */
    ActivityType("/gc/multilingual/ActivityType-CodeList.gc"),
    /**
     * Amount Type CodeList
     */
    AmountType("/gc/multilingual/AmountType-CodeList.gc"),
    /**
     * Bid Type CodeList
     */
    BidType("/gc/multilingual/BidType-CodeList.gc"),
    /**
     * Set whether an answer to a Criterion Property or/and Evidence can be publicly accessible or not
     */
    ConfidentialityLevel("/gc/multilingual/ConfidentialityLevel-CodeList.gc"),
    /**
     * Contract Type CodeList
     */
    ContractType("/gc/multilingual/ContractType-CodeList.gc"),
    /**
     * Country Identification according to ISO 3A
     */
    CountryIdentification("/gc/multilingual/CountryCodeIdentifier-CodeList.gc"),
    /**
     * Criterion Element Type CodeList
     */
    CriterionElementType("/gc/multilingual/CriterionElementType-CodeList.gc"),
    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/multilingual/CurrencyCode-CodeList.gc"),
    /**
     * Document Reference types, used by ESPD Request and Response to add specific external references required by
     * the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/multilingual/DocRefContentType-CodeList.gc"),
    /**
     * States the type of identifier of an Economic Operator of a group of tenderers
     */
    EOIDType("/gc/multilingual/EOIDType-CodeList.gc"),
    /**
     * Economic Operator Industry Classification CodeList
     */
    EOIndustryClassification("/gc/multilingual/EOIndustryClassificationCode-CodeList.gc"),
    /**
     * Language iso code Codelist
     */
    LanguageCodeEU("/gc/multilingual/EULanguageCode-CodeList.gc"),
    /**
     * Specifies whether the Criterion is weigthed or a pass/fail one
     */
    EvaluationMethodType("/gc/multilingual/EvaluationMethodType-CodeList.gc"),
    /**
     * Financial Ratio Type CodeList
     */
    FinancialRatioType("/gc/multilingual/FinancialRatioType-CodeList.gc"),
    /**
     * Criterion Jurisdiction Level for the ESPD Criteria Codelist
     */
    LegislationType("/gc/multilingual/LegislationType-CodeList.gc"),
    /**
     * Qualification Application Type, created in order to specify whether the ESPD is "Regulated" or
     * "Self-contained"
     */
    QualificationApplicationType("/gc/multilingual/QualificationApplicationType-CodeList.gc"),
    /**
     * Response Data type codelist, used to infer the type of response a requirement requires.
     */
    ResponseDataType("/gc/multilingual/ResponseDataType-CodeList.gc"),
    PeriodMeasureType("/gc/multilingual/PeriodMeasureType-CodeList.gc"),
    ProcedureType("/gc/multilingual/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/multilingual/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/multilingual/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/multilingual/ServicesProjectSubType-CodeList.gc"),
    TechnicalCapabilityType("/gc/multilingual/TechnicalCapabilityType-CodeList.gc"),
    EORoleType("/gc/multilingual/EORoleType-CodeList.gc"),
    PropertyGroupType("/gc/multilingual/PropertyGroupType-CodeList.gc");

    private final String name;
    private volatile GenericCode INSTANCE;

    private static final String DEFAULT_LANG = "eng";
    private String lang;

    private MultilingualCodeList(String name) {
        this.lang = DEFAULT_LANG;
        this.name = name;
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

    /**
     *
     * @param otherName
     * @return true if the name of the codelist is equal with @param othername
     *
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

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void initLang() {
        lang = DEFAULT_LANG;
    }

    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id, lang);
    }
        
    /**
     * @param data
     * @return the Id of the codelist for the specific value
     */
    @Override
    public final String getIdForData(String data) {
        return getInstance().getIdForData(data, lang);
    }

    /**
     *
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id, lang);
    }

    /**
     *
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value, lang);
    }

    /**
     * @return the internal representation of the codelist as an immutable bimap
     */
    @Override
    public final BiMap<String, String> getBiMap() {
        return getInstance().getBiMap(lang);
    }
    
    @Override
    public Set<String> getAllLangs() {
        return getInstance().getAllLangs();
    }
    
}
