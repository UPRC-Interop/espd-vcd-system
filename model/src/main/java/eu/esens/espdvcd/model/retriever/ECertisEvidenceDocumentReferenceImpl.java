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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( 
        {
            "attachment", 
            "validityPeriod", 
            "issuerParty"
        })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ECertisEvidenceDocumentReferenceImpl implements ECertisEvidenceDocumentReference {
        
    private ECertisAttachment attachment;
    private ECertisValidityPeriod validityPeriod;
    private ECertisIssuerParty issuerParty;
    
    @Override
    @JsonDeserialize(as = ECertisAttachmentImpl.class)
    public void setAttachment(ECertisAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    @JsonProperty("Attachment")
    @JsonAlias({"attachment"})
    public ECertisAttachment getAttachment() {
        return attachment;
    }

    @Override
    @JsonDeserialize(as = ECertisValidityPeriodImpl.class)
    public void setValidityPeriod(ECertisValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    @JsonProperty("ValidityPeriod")
    @JsonAlias({"validityPeriod"})
    public ECertisValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    @Override
    @JsonDeserialize(as = ECertisIssuerPartyImpl.class)
    public void setIssuerParty(ECertisIssuerParty issuerParty) {
        this.issuerParty = issuerParty;
    }

    @Override
    @JsonProperty("IssuerParty")
    @JsonAlias({"issuerParty"})
    public ECertisIssuerParty getIssuerParty() {
        return issuerParty;
    }
       
}
