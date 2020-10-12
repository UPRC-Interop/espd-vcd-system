/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( 
        {
            "websiteURI", 
            "partyName"
        })
public class ECertisEvidenceIssuerPartyImpl implements ECertisEvidenceIssuerParty {
       
    private String websiteURI;
    private List<ECertisPartyName> partyName;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }

    @Override
    @JsonProperty("WebsiteURI")
    public String getWebsiteURI() {
        return websiteURI;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisPartyNameImpl.class)
    public void setPartyName(List<ECertisPartyName> partyName) {
        this.partyName = partyName;
    }

    @Override
    @JsonProperty("PartyName")
    public List<ECertisPartyName> getPartyName() {
        if (partyName == null) {
            partyName = new ArrayList<>();
        }
        return partyName;
    }

}
