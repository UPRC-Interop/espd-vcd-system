package eu.esens.espdvcd.model.retriever.interfaces;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisEvidenceGroup {
    
//    IECertisID getID();
//    
//    void setID(IECertisID ID);
    
    String getID();
    
    void setID(String ID);
    
    List<IECertisEvidence> getEvidences(); 
    
    void setEvidences(List<IECertisEvidence> evidences);
    
}
