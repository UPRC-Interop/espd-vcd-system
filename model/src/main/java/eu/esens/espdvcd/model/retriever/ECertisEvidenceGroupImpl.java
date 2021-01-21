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
package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
 @JsonPropertyOrder( 
         {
             "ID", 
             "evidences"
         })
 @JsonIgnoreProperties(ignoreUnknown = true)
 public class ECertisEvidenceGroupImpl implements ECertisEvidenceGroup {
       
    private String ID; 
    private List<ECertisEvidence> evidences;

    @Override
    @JsonProperty("ID")
    @JsonAlias({"id", "Id"})
    public String getID() {
        return ID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setID(String ID) {
       this.ID = ID;
    }
    
    @Override
    @JsonProperty("TypeOfEvidence")
    @JsonAlias({"typeOfEvidence"})
    public List<ECertisEvidence> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        return evidences;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceImpl.class)
    public void setEvidences(List<ECertisEvidence> evidences) {
        this.evidences = evidences;
    }
    
}
