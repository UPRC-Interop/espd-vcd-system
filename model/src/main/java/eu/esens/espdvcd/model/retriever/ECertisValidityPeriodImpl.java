package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomArrayTextValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisValidityPeriodImpl implements ECertisValidityPeriod {
        
    private String description;
    
    @Override
    @JsonDeserialize(using = CustomArrayTextValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }
    
}
