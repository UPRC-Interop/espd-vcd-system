package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisLegislationReference;
import eu.esens.espdvcd.model.util.CustomTextValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "title",
            "description",
            "jurisdictionLevelCode",
            "article",
            "URI"
        })
public class ECertisLegislationReference extends LegislationReference implements IECertisLegislationReference {

    public ECertisLegislationReference() {
        super(null, null, null, null, null);
    }
    
    public ECertisLegislationReference(String title, String description, String jurisdictionLevelCode, String article, String URI) {
        super(title, description, jurisdictionLevelCode, article, URI);
    }
    
    @Override
    @JsonProperty("Title")
    public String getTitle() {
        return title;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("JurisdictionLevelCode")
    public String getJurisdictionLevelCode() {
        return jurisdictionLevelCode;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setJurisdictionLevelCode(String JurisdictionLevelCode) {
        this.jurisdictionLevelCode = JurisdictionLevelCode;
    }

    @Override
    @JsonProperty("Article")
    public String getArticle() {
        return article;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setURI(String URI) {
        this.URI = URI;
    }

}
