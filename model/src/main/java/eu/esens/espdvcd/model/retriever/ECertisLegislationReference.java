package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisLegislationReference;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisURI;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"title", "description", "jurisdictionLevelCode", "article", "URI"} )
public class ECertisLegislationReference implements IECertisLegislationReference {
        
    private IECertisText title;
    private IECertisText description;
    private String jurisdictionLevelCode;
    private IECertisText article;
    private IECertisURI URI;
    
    
    @Override
    @JsonProperty("Title")
    public IECertisText getTitle() {
        return title;
    }

    @Override
    @JsonDeserialize(as = ECertisText.class)
    public void setTitle(IECertisText title) {
        this.title = title;
    }

    @Override
    @JsonProperty("Description")
    public IECertisText getDescription() {
        return description;
    }

    @Override
    @JsonDeserialize(as = ECertisText.class)
    public void setDescription(IECertisText description) {
        this.description = description;
    }

    @Override
    @JsonProperty("JurisdictionLevelCode")
    public String getJurisdictionLevelCode() {
        return jurisdictionLevelCode;
    }

    @Override
    public void setJurisdictionLevelCode(String JurisdictionLevelCode) {
        this.jurisdictionLevelCode = JurisdictionLevelCode;
    }

    @Override
    @JsonProperty("Article")
    public IECertisText getArticle() {
        return article;
    }

    @Override 
    @JsonDeserialize(as = ECertisText.class)
    public void setArticle(IECertisText article) {
        this.article = article;
    }

    @Override
    @JsonProperty("URI")
    public IECertisURI getURI() {
        return URI;
    }

    @Override
    @JsonDeserialize(as = ECertisURI.class)
    public void setURI(IECertisURI URI) {
        this.URI = URI;
    }
       
}

