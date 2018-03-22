package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisAttachmentImpl implements ECertisAttachment {
        
    private ECertisExternalReference externalReference;
    
    @Override
    @JsonDeserialize(as = ECertisExternalReferenceImpl.class)
    public void setExternalReference(ECertisExternalReference externalReference) {
        this.externalReference = externalReference;
    }

    @Override
    @JsonProperty("ExternalReference")
    public ECertisExternalReference getExternalReference() {
        return externalReference;
    }
    
}
