package eu.esens.espdvcd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class LegislationReference {


    /**
     * Legislation title
     * <p>
     * Title of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-064, tir92-130<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Title<br>
     */

    @NotNull
    protected String title;

    /**
     * Legislation description
     * <p>
     * Textual description of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-065, tir92-131<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Description<br>
     */
    protected String description;

    /**
     * Jurisdiction level
     * <p>
     * Jurisdictional level of a particular legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-066, tir92-132<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.JurisdictionLevelCode<br>
     */
    protected String jurisdictionLevelCode;

    /**
     * Legislation article
     * <p>
     * Textual description of the article of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-067, tir92-133<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Article<br>
     */
    protected String article;

    /**
     * Legislation URI
     * <p>
     * URI that points to a particular legislation	Identifier
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-068, tir92-134<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.URI<br>
     */
    protected String URI;

    public LegislationReference(@JsonProperty("title") String title, @JsonProperty("desccription") String description,
                                @JsonProperty("jurisdictionLevelCode") String jurisdictionLevelCode,
                                @JsonProperty("article") String article, @JsonProperty("URI") String URI) {
        this.title = title;
        this.description = description;
        this.jurisdictionLevelCode = jurisdictionLevelCode;
        this.article = article;
        this.URI = URI;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJurisdictionLevelCode() {
        return jurisdictionLevelCode;
    }

    public void setJurisdictionLevelCode(String jurisdictionLevelCode) {
        this.jurisdictionLevelCode = jurisdictionLevelCode;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
}
