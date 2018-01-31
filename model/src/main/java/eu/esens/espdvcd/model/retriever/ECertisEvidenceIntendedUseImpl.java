package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisEvidenceIntendedUseImpl implements ECertisEvidenceIntendedUse {
        
    private String description;
    
    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }
    
}
