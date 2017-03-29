package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"websiteURI", "partyName"} )
public class ECertisEvidenceIssuerPartyImpl implements ECertisEvidenceIssuerParty {
       
    private String websiteURI;
    private List<ECertisPartyName> partyName;

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
    @JsonDeserialize(as = List.class, contentAs = ECertisPartyNameImpl.class)
    public void setPartyName(List<ECertisPartyName> partyName) {
        this.partyName = partyName;
    }

    @Override
    @JsonProperty("PartyName")
    public List<ECertisPartyName> getPartyName() {
        if (partyName == null) {
            partyName = new ArrayList<>();
        }
        return partyName;
    }

}
