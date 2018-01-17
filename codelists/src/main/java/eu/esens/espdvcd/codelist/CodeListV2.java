package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;
import java.util.Set;

/**
 *
 * @author Konstantinos Raptis
 */
public enum CodeListV2 implements CodeList {

    /**
     * Activity Type CodeList
     */
    ActivityType("/gc/v2/ActivityType-CodeList.gc"),
    /**
     * Amount Type CodeList
     */
    AmountType("/gc/v2/AmountType-CodeList.gc"),
    /**
     * Set whether an answer to a Criterion Property or/and Evidence can be
     * publicly accessible or not
     */
    ConfidentialityLevel("/gc/v2/ConfidentialityLevel-CodeList.gc"),
    /**
     * Contract Type CodeList
     */
    ContractType("/gc/v2/ContractType-CodeList.gc"),
    /**
     * Country Identification according to ISO 3A
     */
    CountryIdentification("/gc/v2/CountryCodeIdentifier-CodeList.gc"),
    /**
     * ESPD Criteria Type taxonomy, flattened as a Codelist
     */
    CriteriaType("/gc/v2/CriteriaType-CodeList.gc"),
    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/v2/CurrencyCode-CodeList.gc"),
    /**
     * Document Reference types, used by ESPD Request and Response to add
     * specific external references required by the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/v2/DocRefContentType-CodeList.gc"),
    /**
     * States the type of identifier of an Economic Operator of a group of
     * tenderers
     */
    EOIDType("/gc/v2/EOIDType-CodeList.gc"),
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
     * Speficies the jurisdiction level of a gazette or publication board where
     * the contract documents of the procurement procedure have been published
     */
    JurisdictionLevel("/gc/v2/JurisdictionLevel-CodeList.gc"),
    /**
     * Criterion Jurisdiction Level for the ESPD Criteria Codelist
     */
    LegislationType("/gc/v2/LegislationType-CodeList.gc"),
    /**
     * Specifies the number of lots the economic operator may tender
     */
    NumberOfLotsCode("/gc/v2/NumberOfLotsCode-CodeList.gc"),
    /**
     * Qualification Application Type, created in order to specify whether the
     * ESPD is "Regulated" or "Self-contained"
     */
    QualificationApplicationType("/gc/v2/QualificationApplicationType-CodeList.gc"),
    /**
     * Response Data type codelist, used to infer the type of response a
     * requirement requires.
     */
    ResponseDataType("/gc/v2/ResponseDataType-CodeList.gc"),
    PeriodMeasureType("/gc/v2/PeriodMeasureType-CodeList.gc"),
    ProcedureType("/gc/v2/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/v2/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/v2/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/v2/ServicesProjectSubType-CodeList.gc"),
    TechnicalCapabilityType("/gc/v2/TechnicalCapabilityType-CodeList.gc"),
    EORoleType("/gc/v2/EORoleType-CodeList.gc");

    private final String name;

    private volatile GenericCode INSTANCE;

    private CodeListV2(String s) {
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

    /**
     * @param data
     * @return the Id of the codelist for the specific value
     */
    @Override
    public final String getIdForData(String data) {
        return getInstance().getIdForData(data, "en");
    }

    /**
     *
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id, "en");
    }

    /**
     *
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value, "en");
    }

    /**
     *
     * @param id
     * @return the value mapped with the specific id in the codelist, null
     * otherwise
     */
    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id, "en");
    }

    /**
     * @return the internal representation of the codelist as an immutable bimap
     */
    @Override
    public final BiMap<String, String> getBiMap() {
        return getInstance().getBiMap("en");
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
    
    @Override
    public Set<String> getAllLangs() {
        return getInstance().getAllLangs();
    }
    
}
