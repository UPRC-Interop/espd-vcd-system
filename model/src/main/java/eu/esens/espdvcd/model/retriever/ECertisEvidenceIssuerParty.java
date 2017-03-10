package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisPartyName;
import java.util.List;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceIssuerParty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisWebsiteURI;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"websiteURI", "partyName"} )
public class ECertisEvidenceIssuerParty implements IECertisEvidenceIssuerParty {
        
    private IECertisWebsiteURI websiteURI;
    private List<IECertisPartyName> partyName;
    
    @Override
    @JsonDeserialize(as = ECertisWebsiteURI.class)
    public void setWebsiteURI(IECertisWebsiteURI websiteURI) {
        this.websiteURI = websiteURI;
    }

    @Override
    @JsonProperty("WebsiteURI")
    public IECertisWebsiteURI getWebsiteURI() {
        return websiteURI;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisPartyName.class)
    public void setPartyName(List<IECertisPartyName> partyName) {
        this.partyName = partyName;
    }

    @Override
    @JsonProperty("PartyName")
    public List<IECertisPartyName> getPartyName() {
        return partyName;
    }

}
