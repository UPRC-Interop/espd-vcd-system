package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidence;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceGroup;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
 @JsonPropertyOrder( {"ID", "evidences"} )
 public class ECertisEvidenceGroup implements IECertisEvidenceGroup {

    private String ID; 
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidence.class)
    private List<IECertisEvidence> evidences;

    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String ID) {
       this.ID = ID;
    }
    
    @Override
    @JsonProperty("TypeOfEvidence")
    public List<IECertisEvidence> getEvidences() {
        return evidences;
    }

    @Override
    public void setEvidences(List<IECertisEvidence> evidences) {
        this.evidences = evidences;
    }
        
}
