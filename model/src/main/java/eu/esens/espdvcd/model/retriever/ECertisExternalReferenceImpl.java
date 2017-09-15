package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisExternalReferenceImpl implements ECertisExternalReference {
    
    private String URI;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setURI(String URI) {
        this.URI = URI;
    }

    @Override
    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }
        
}
