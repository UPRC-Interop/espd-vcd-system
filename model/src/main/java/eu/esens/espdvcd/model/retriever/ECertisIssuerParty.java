package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisIssuerParty;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisIssuerParty implements IECertisIssuerParty {

    private String websiteURI;
    
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
        
}
