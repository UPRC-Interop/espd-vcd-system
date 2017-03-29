package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
 @JsonPropertyOrder( {"ID", "evidences"} )
 public class ECertisEvidenceGroupImpl implements ECertisEvidenceGroup {
       
    private String ID; 
    private List<ECertisEvidence> evidences;

    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setID(String ID) {
       this.ID = ID;
    }
    
    @Override
    @JsonProperty("TypeOfEvidence")
    public List<ECertisEvidence> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        return evidences;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceImpl.class)
    public void setEvidences(List<ECertisEvidence> evidences) {
        this.evidences = evidences;
    }
        
}
