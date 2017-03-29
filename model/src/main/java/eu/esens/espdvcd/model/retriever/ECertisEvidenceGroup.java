package eu.esens.espdvcd.model.retriever;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisEvidenceGroup {
    
    String getID();
    
    void setID(String ID);
       
    List<ECertisEvidence> getEvidences(); 
    
    void setEvidences(List<ECertisEvidence> evidences);
    
}
