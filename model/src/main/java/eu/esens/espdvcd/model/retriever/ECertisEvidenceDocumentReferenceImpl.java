package eu.esens.espdvcd.model.retriever;

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
    public ECertisIssuerParty getIssuerParty() {
        return issuerParty;
    }
       
}
