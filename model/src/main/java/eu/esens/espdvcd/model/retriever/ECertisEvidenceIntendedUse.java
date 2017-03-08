package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceIntendedUse;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisEvidenceIntendedUse implements IECertisEvidenceIntendedUse {
        
    private String description;
    
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }
    
}
