package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;

/**
 * CodeList interface should be implemented by all codeLists implementation 
 * enum classes in order to provide an "extendable" enum design. Normally every
 * implementation class represents a codelists version.
 * 
 */
public interface CodeList {
        
    boolean equalsName(String otherName);
        
    BiMap<String, GenericCode.CodelistRow> getBiMap();
        
    String getConstantName();
    
    String getDataForId(String id);
    
    String getIdForData(String data);
    
    boolean containsId(String id);
    
    boolean containsValue(String value);
    
}
