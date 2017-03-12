package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisValidityPeriod;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisValidityPeriod implements IECertisValidityPeriod {
        
    private List<IECertisText> description;
    
    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisText.class)
    public void setDescription(List<IECertisText> description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public List<IECertisText> getDescription() {
        if (description == null) {
            description = new ArrayList<>();
        }
        return description;
    }
    
}
