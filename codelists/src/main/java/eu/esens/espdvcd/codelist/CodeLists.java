package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * CodeLists interface should be implemented by all codeListsVX enum classes  
 * 
 */
public interface CodeLists {
    
    /**
     *
     * @param otherName
     * @return true if the name of the codelist is equal with @param othername
     *
     */
    boolean equalsName(String otherName);
    
    /**
     * @param data
     * @return the Id of the codelist for the specific value
     */
    String getIdForData(String data);
    
    /**
     *
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    boolean containsId(String id);
    
    /**
     *
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    boolean containsValue(String value);
    
    /**
     *
     * @param id
     * @return the value mapped with the specific id in the codelist, null
     * otherwise
     */
    String getValueForId(String id);
    
    /**
     * @return the internal representation of the codelist as an immutable bimap
     */
    BiMap<String, String> getBiMap();
    
    /**
     * This method work as top level of ENUM_CONSTANT.name() method
     * 
     * @return the exact name of this ENUM_CONSTANT
     */
    String getConstantName();
    
}
