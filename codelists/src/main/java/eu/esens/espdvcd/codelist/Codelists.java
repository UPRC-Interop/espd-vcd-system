package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * Multiton implementation for Code Lists  
 * 
 */
public enum Codelists {

    /**
     * Activity Type CodeList
     */
    ActivityType ("/gc/ActivityTypeCode-CodeList.gc"),

    /**
     * Amount Type CodeList
     */
    AmountType("/gc/AmountTypeCodes-CodeList.gc"),

    /**
     * Contract Type CodeList
     */
    ContractType ("/gc/ContractType-CodeList.gc"),

    /**
    * Country Identification according to ISO 3A
    */
    CountryIdentification ("/gc/CountryCodeIdentifier-CodeList.gc"),

    /**
     * ESPD Criteria Type taxonomy, flattened as a Codelist 
     */
    CriteriaType ("/gc/CriteriaTypeCodes-CodeList.gc"),

    /**
    *Criterion Jurisdiction Level for the ESPD Criteria Codelist    
    */
    CriterionJurisdictionLevel ("/gc/CriterionJurisdictionLevel-CodeList.gc"),

    /**
     * UBL Cusomization ID codelist, used for the ESPD Request and Response Artifacts
     */
    CustomizationID("/gc/CustomizationID-CodeList.gc"),

    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/CurrencyCode-CodeList.gc"),

    /**
     * Document Reference types, used by ESPD Request and Response to add specific
     * external references required by the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/DocRefContentType-CodeList.gc"),

    /**
     * Economic Operator Role Codelist
     */
    EORole("/gc/EORoleCodes-CodeList.gc"),

    /**
     * Language iso code Codelist
     */
    LanguageCodeEU("/gc/LanguageCodeEU-CodeList.gc"),

    /**
     * Response Data type codelist, used to infer the type of response a requirement
     * requires.
     */
    ResponseDataType("/gc/ResponseDataType-CodeList.gc"),
    PeriodMeasureType("/gc/PeriodMeasureTypeCodes-CodeList.gc"),
    PeriodType("/gc/PeriodTypeCodes-CodeList.gc"),
    ProcedureType("/gc/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/ServicesProjectSubType-CodeList.gc"),
    TechnicalCapabilityType("/gc/TechnicalCapabilityTypeCode-CodeList.gc"),
    TenderingRole ("/gc/TendererRole-CodeList.gc");

    private final String name;       

    private volatile GenericCode INSTANCE;

    private Codelists(String s) {
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
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    /**
     * @param data
     * @return the Id of the codelist for the specific value
     */
    public final String getIdForData(String data) {
        return getInstance().getIdForData(data);
    }
    
    /**
     *
     * @param id 
     * @return true if the codelist contains the specific id, false otherwise
     */
    public final boolean containsId(String id) {
        return getInstance().containsId(id);
    }

    /**
     *
     * @param value 
     * @return true if the codelist contains the specific value, false otherwise
     */
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value);
    }   
    
    /**
     *
     * @param id 
     * @return the value mapped with the specific id in the codelist, null otherwise
     */
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id);
    }

    /**
     * @return the internal representation of the codelist as an immutable bimap
     */   
    public final BiMap<String, String> getBiMap() {
        return getInstance().getBiMap();
    }

    @Override
    public String toString() {
       return this.name;
    }
    
}


