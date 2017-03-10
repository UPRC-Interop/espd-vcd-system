package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisURI;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisURI implements IECertisURI {
    
    private String URI;
        
    public ECertisURI() {
        
    }
    
    public ECertisURI(String URI) {
        this.URI = URI;
    }
    
    @Override
    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }

    @Override
    public void setURI(String URI) {
        this.URI = URI;
    }
    
}
