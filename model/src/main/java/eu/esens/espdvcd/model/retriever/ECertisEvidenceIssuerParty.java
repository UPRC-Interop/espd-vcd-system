package eu.esens.espdvcd.model.retriever;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisEvidenceIssuerParty {
    
//    void setWebsiteURI(IECertisWebsiteURI websiteURI);
//        
//    IECertisWebsiteURI getWebsiteURI();
    
    void setWebsiteURI(String websiteURI);
    
    String getWebsiteURI();
    
    void setPartyName(List<ECertisPartyName> partyName);
    
    List<ECertisPartyName> getPartyName();
       
}
