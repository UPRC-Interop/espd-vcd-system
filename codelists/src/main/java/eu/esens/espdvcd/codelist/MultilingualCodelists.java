package eu.esens.espdvcd.codelist;

import java.util.Map;

/**
 *
 * @author Konstantinos Raptis
 */
public interface MultilingualCodelists extends Codelists {
    
    Map<String, String> getDataMap(String lang);
    
    String getValueForId(String id, String lang);
    
    boolean containsId(String id, String lang);
    
    boolean containsValue(String value, String lang);
    
}
