package eu.esens.espdvcd.model.retriever.interfaces;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisEvidenceIssuerParty {
        
    void setWebsiteURI(String websiteURI);
        
    String getWebsiteURI();
    
    void setPartyName(List<IECertisPartyName> partyName);
    
    List<IECertisPartyName> getPartyName();
       
}
