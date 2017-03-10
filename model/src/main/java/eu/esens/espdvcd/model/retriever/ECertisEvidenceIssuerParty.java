package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisPartyName;
import java.util.List;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceIssuerParty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisStringWrapper;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"websiteURI", "partyName"} )
public class ECertisEvidenceIssuerParty implements IECertisEvidenceIssuerParty {
    
    @JsonDeserialize(as = ECertisStringWrapper.class)
    private IECertisStringWrapper websiteURI;
    @JsonDeserialize(as = List.class, contentAs = ECertisPartyName.class)
    private List<IECertisPartyName> partyName;
    
    @Override
    public void setWebsiteURI(IECertisStringWrapper websiteURI) {
        this.websiteURI = websiteURI;
    }

    @Override
    @JsonProperty("WebsiteURI")
    public IECertisStringWrapper getWebsiteURI() {
        return websiteURI;
    }

    @Override
    public void setPartyName(List<IECertisPartyName> partyName) {
        this.partyName = partyName;
    }

    @Override
    @JsonProperty("PartyName")
    public List<IECertisPartyName> getPartyName() {
        return partyName;
    }

}
