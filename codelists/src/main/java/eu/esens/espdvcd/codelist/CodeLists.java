package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * CodeLists interface should be implemented by all codeLists(x version)Impl enum classes  
 * 
 */
public interface CodeLists {
        
    boolean equalsName(String otherName);
        
    String getIdForData(String data);
        
    boolean containsId(String id);
        
    boolean containsValue(String value);
        
    String getValueForId(String id);
        
    BiMap<String, String> getBiMap();
        
    String getConstantName();
    
}
