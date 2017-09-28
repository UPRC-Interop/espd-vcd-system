package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 *
 * @author Konstantinos Raptis
 */
public enum CodeListsTestImpl implements CodeLists {

    NotyceType("/gc/test/notice-type.gc");
    
    private final String name;
    private volatile GenericCode INSTANCE;
    
    private static final String DEFAULT_LANG = "en";
    private String lang;
    
    private CodeListsTestImpl(String name) {
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
    
    /**
     * @param data
     * @return the Id of the codelist for the specific value
     */
    @Override
    public final String getIdForData(String data) {
        return getInstance().getIdForData(data);
    }
   
    /**
     *
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id);
    }

    /**
     *
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value);
    }
    
    /**
     *
     * @param id
     * @return the value mapped with the specific id in the codelist, null
     * otherwise
     */
    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id);
    }
    
    /**
     * @return the internal representation of the codelist as an immutable bimap
     */
    @Override
    public final BiMap<String, String> getBiMap() {
        return getInstance().getBiMap();
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

    @Override
    public String getNameForIdV2(String id) {
        return getInstance().getNameForIdV2(id);
    }

    @Override
    public String getDescriptionForIdV2(String id) {
        if (lang == null) {
            lang = DEFAULT_LANG;
        }
        return getInstance().getDescriptionForIdV2(id, lang);
    }

    @Override
    public String getIdForDataV2(String data) {
        return getInstance().getIdForDataV2(data);
    }

    @Override
    public boolean containsIdV2(String id) {
        return getInstance().containsIdV2(id);
    }

    @Override
    public boolean containsValueV2(String value) {
        return getInstance().containsValueV2(value);
    }
       
}
