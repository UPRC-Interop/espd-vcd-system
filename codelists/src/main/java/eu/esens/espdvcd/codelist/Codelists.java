package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * Multiton implementation for Code Lists
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public enum Codelists {
    ActivityType ("/gc/ActivityTypeCode-CodeList.gc"),
    ContractType ("/gc/ContractType-CodeList.gc"),
    CountryIdentification ("/gc/CountryIdentificationCode-2.1.gc"),
    CriteriaType ("/gc/CriteriaTypeCode-CodeList.gc"),
    CriterionJurisdictionLevel ("/gc/CriterionJurisdictionLevel-CodeList.gc"),
    CustomizationID("/gc/CustomizationID-Codelist.gc"),
    EORole("/gc/EORoleCodes-Codelist.gc"),
    LanguageCodeEU("/gc/LanguageCodeEU-Codelist.gc"),
    ProcedureType("/gc/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/ProjectType-CodeList.gc"),
    ResponseDataType("/gc/ResponseDataType-CodeList.gc"),
    ServicesProjectSubType("/gc/ServicesProjectSubType-CodeList.gc"),
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


