package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisAttachment;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisValidityPeriod;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceDocumentReference;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisIssuerParty;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"attachment", "validityPeriod", "issuerParty"} )
public class ECertisEvidenceDocumentReference implements IECertisEvidenceDocumentReference {
        
    private IECertisAttachment attachment;
    private IECertisValidityPeriod validityPeriod;
    private IECertisIssuerParty issuerParty;
    
    @Override
    @JsonDeserialize(as = ECertisAttachment.class)
    public void setAttachment(IECertisAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    @JsonProperty("Attachment")
    public IECertisAttachment getAttachment() {
        return attachment;
    }

    @Override
    @JsonDeserialize(as = ECertisValidityPeriod.class)
    public void setValidityPeriod(IECertisValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    @JsonProperty("ValidityPeriod")
    public IECertisValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    @Override
    @JsonDeserialize(as = ECertisIssuerParty.class)
    public void setIssuerParty(IECertisIssuerParty issuerParty) {
        this.issuerParty = issuerParty;
    }

    @Override
    @JsonProperty("IssuerParty")
    public IECertisIssuerParty getIssuerParty() {
        return issuerParty;
    }
       
}
