package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.retriever.interfaces.IECertisWebsiteURI;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisWebsiteURI implements IECertisWebsiteURI {
    
    private String websiteURI;

    public ECertisWebsiteURI() {
    }

    public ECertisWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }
    
    @Override
    public String getWebsiteURI() {
        return websiteURI;
    }

    @Override
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }
        
}
