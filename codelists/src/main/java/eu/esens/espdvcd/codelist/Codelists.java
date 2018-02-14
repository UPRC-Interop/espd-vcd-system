package eu.esens.espdvcd.codelist;

import java.util.Map;
import java.util.Optional;

/**
 * CodeList interface should be implemented by all codeLists implementation 
 * enum classes in order to provide an "extendable" enum design. Normally every
 * implementation class represents a codelists version.
 * 
 */
public interface Codelists {
        
    boolean equalsName(String otherName);
        
    Map<String, String> getDataMap();
        
    String getConstantName();
    
    String getValueForId(String id);
    
    boolean containsId(String id);
    
    boolean containsValue(String value);

    Optional<Map<String, String>> _getDataMap();

    Optional<String> _getValueForId(String id);

}
