/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.codelist;

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
