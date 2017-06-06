package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * CodeLists interface should be implemented by all codeListsVX enum classes  
 * 
 */
public interface CodeLists {
    
    boolean equalsName(String otherName);
    
    String getIdForData(String data);
    
    boolean containsId(String id);
    
    boolean containsValue(String value);
    
    String getValueForId(String id);
    
    BiMap<String, String> getBiMap();
    
    /**
     * This method work as top level of ENUM_CONSTANT.name() method
     * 
     * @return the exact name of this ENUM_CONSTANT
     */
    String getConstantName();
    
}
