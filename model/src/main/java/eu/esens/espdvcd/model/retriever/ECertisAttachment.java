package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisAttachment;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisExternalReference;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisAttachment implements IECertisAttachment {
    
    @JsonDeserialize(as = ECertisExternalReference.class)
    private IECertisExternalReference externalReference;
    
    @Override
    public void setExternalReference(IECertisExternalReference externalReference) {
        this.externalReference = externalReference;
    }

    @Override
    @JsonProperty("ExternalReference")
    public IECertisExternalReference getExternalReference() {
        return externalReference;
    }
    
}
