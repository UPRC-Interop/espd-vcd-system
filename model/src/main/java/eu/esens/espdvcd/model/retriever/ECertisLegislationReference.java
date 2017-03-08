package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisLegislationReference;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"title", "description", "jurisdictionLevelCode", "article", "URI"} )
public class ECertisLegislationReference implements IECertisLegislationReference {
    
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText title;
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText description;
    private String jurisdictionLevelCode;
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText article;
    private String URI;
    
    @Override
    @JsonProperty("Title")
    public IECertisText getTitle() {
        return title;
    }

    @Override
    public void setTitle(IECertisText title) {
        this.title = title;
    }

    @Override
    @JsonProperty("Description")
    public IECertisText getDescription() {
        return description;
    }

    @Override
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
    public void setArticle(IECertisText article) {
        this.article = article;
    }

    @Override
    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }

    @Override
    public void setURI(String URI) {
        this.URI = URI;
    }
        
}
