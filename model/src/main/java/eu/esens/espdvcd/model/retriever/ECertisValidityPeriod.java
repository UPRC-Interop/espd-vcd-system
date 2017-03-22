package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisValidityPeriod;
import eu.esens.espdvcd.model.util.CustomTextValueDeserializer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisValidityPeriod implements IECertisValidityPeriod {
        
    private List<String> description;
    
    @Override
    @JsonDeserialize(as = List.class, using = CustomTextValueDeserializer.class, contentAs = String.class)
    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public List<String> getDescription() {
        if (description == null) {
            description = new ArrayList<>();
        }
        return description;
    }
    
}
