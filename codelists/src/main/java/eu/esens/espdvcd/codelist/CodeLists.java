package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * CodeLists interface should be implemented by all codeLists implementation 
 * enum classes in order to provide an "extendable" enum design. Normally every
 * implementation class represents a codelists version.
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
   
    String getNameForIdV2(String id);
    
    String getDescriptionForIdV2(String id);
    
    String getIdForDataV2(String data);
    
}
