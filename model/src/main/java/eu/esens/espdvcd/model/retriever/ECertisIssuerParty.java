package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisIssuerParty;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisIssuerParty implements IECertisIssuerParty {

    private String websiteURI;
    
    @Override
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }

    @Override
    @JsonProperty("WebsiteURI")
    public String getWebsiteURI() {
        return websiteURI;
    }
        
}
