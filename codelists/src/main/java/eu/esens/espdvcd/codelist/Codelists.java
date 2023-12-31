/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
