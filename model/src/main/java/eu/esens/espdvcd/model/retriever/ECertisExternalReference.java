package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisExternalReference;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisExternalReference implements IECertisExternalReference {
    
    private String URI;

    @Override
    public void setURI(String URI) {
        this.URI = URI;
    }

    @Override
    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }
        
}
