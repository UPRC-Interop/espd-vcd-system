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
package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisExternalReferenceImpl implements ECertisExternalReference {
    
    private String URI;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setURI(String URI) {
        this.URI = URI;
    }

    @Override
    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }
        
}
