package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * Multiton implementation for Code Lists
 * 
 */
public enum Codelists {
    ActivityType ("/gc/ActivityTypeCode-CodeList.gc"),
    AmountType("/gc/AmountTypeCodes-CodeList.gc"),
    ContractType ("/gc/ContractType-CodeList.gc"),
    CountryIdentification ("/gc/CountryCodeIdentifier-CodeList.gc"),
    CriteriaType ("/gc/CriteriaTypeCodes-CodeList.gc"),
    CriterionJurisdictionLevel ("/gc/CriterionJurisdictionLevel-CodeList.gc"),
    CustomizationID("/gc/CustomizationID-CodeList.gc"),
    Currency("/gc/CurrencyCode-CodeList.gc"),
    DocumentReferenceContentType("/gc/DocRefContentType-CodeList.gc"),
    EORole("/gc/EORoleCodes-CodeList.gc"),
    LanguageCodeEU("/gc/LanguageCodeEU-CodeList.gc"),
    PeriodMeasureType("/gc/PeriodMeasureTypeCodes-CodeList.gc"),
    PeriodType("/gc/PeriodTypeCodes-CodeList.gc"),
    ProcedureType("/gc/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/ProjectType-CodeList.gc"),
    ResponseDataType("/gc/ResponseDataType-CodeList.gc"),
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

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public final String getIdForData(String data) {
        return getInstance().getIdForData(data);
    }
    
    public final boolean containsId(String id) {
        return getInstance().containsId(id);
    }
    
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value);
    }   

    public final String getValueForId(String id) {
        return getInstance().getValueForId(id);
    }

    public final BiMap<String, String> getBiMap() {
        return getInstance().getBiMap();
    }

    @Override
    public String toString() {
       return this.name;
    }
    
}


