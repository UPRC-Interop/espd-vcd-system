package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisAttachment;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisValidityPeriod;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceDocumentReference;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"attachment", "validityPeriod"} )
public class ECertisEvidenceDocumentReference implements IECertisEvidenceDocumentReference {
    
    @JsonDeserialize(as = ECertisAttachment.class)
    private IECertisAttachment attachment;
    @JsonDeserialize(as = ECertisValidityPeriod.class)
    private IECertisValidityPeriod validityPeriod;

    @Override
    public void setAttachment(IECertisAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    @JsonProperty("Attachment")
    public IECertisAttachment getAttachment() {
        return attachment;
    }

    @Override
    public void setValidityPeriod(IECertisValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    @JsonProperty("ValidityPeriod")
    public IECertisValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }
       
}
