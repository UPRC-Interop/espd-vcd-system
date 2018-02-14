package eu.esens.espdvcd.codelist;

import eu.esens.espdvcd.codelist.Codelists;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Konstantinos Raptis
 */
public interface MultilingualCodelists extends Codelists {
    
    Map<String, String> getDataMap(String lang);
    
    String getValueForId(String id, String lang);
    
    boolean containsId(String id, String lang);
    
    boolean containsValue(String value, String lang);

    Optional<Map<String, String>> _getDataMap(String lang);

    Optional<String> _getValueForId(String id, String lang);

}
