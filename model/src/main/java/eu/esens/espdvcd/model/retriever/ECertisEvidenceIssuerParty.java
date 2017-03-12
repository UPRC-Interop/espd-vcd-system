package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisPartyName;
import java.util.List;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceIssuerParty;
import java.util.ArrayList;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"websiteURI", "partyName"} )
public class ECertisEvidenceIssuerParty implements IECertisEvidenceIssuerParty {
       
    private String websiteURI;
    private List<IECertisPartyName> partyName;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }

    @Override
    @JsonProperty("WebsiteURI")
    public String getWebsiteURI() {
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
        if (partyName == null) {
            partyName = new ArrayList<>();
        }
        return partyName;
    }

}
