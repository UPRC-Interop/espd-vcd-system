package eu.esens.espdvcd.model.retriever.interfaces;

import eu.esens.espdvcd.model.retriever.ECertisID;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisEvidenceGroup {
    
    ECertisID getID();
    
    void setID(ECertisID ID);
       
    List<IECertisEvidence> getEvidences(); 
    
    void setEvidences(List<IECertisEvidence> evidences);
    
}
