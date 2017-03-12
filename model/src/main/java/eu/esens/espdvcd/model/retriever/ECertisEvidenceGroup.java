package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidence;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceGroup;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
 @JsonPropertyOrder( {"ID", "evidences"} )
 public class ECertisEvidenceGroup implements IECertisEvidenceGroup {
       
    private String ID; 
    private List<IECertisEvidence> evidences;

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
    public List<IECertisEvidence> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        return evidences;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidence.class)
    public void setEvidences(List<IECertisEvidence> evidences) {
        this.evidences = evidences;
    }
        
}
