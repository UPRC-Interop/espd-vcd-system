package eu.esens.espdvcd.model.retriever.interfaces;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisEvidenceIssuerParty {
    
    void setWebsiteURI(IECertisWebsiteURI websiteURI);
        
    IECertisWebsiteURI getWebsiteURI();
    
    void setPartyName(List<IECertisPartyName> partyName);
    
    List<IECertisPartyName> getPartyName();
       
}
