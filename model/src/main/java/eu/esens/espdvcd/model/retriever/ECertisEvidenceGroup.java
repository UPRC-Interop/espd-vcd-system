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
       
    private ECertisID ID; 
    private List<IECertisEvidence> evidences;

    @Override
    @JsonProperty("ID")
    public ECertisID getID() {
        return ID;
    }

    @Override
    public void setID(ECertisID ID) {
       this.ID = ID;
    }
    
    @Override
    @JsonProperty("TypeOfEvidence")
    public List<IECertisEvidence> getEvidences() {
        return evidences;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidence.class)
    public void setEvidences(List<IECertisEvidence> evidences) {
        this.evidences = evidences;
    }
        
}
