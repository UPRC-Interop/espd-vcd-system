package eu.esens.espdvcd.model;

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
     * Cardinality: 0..1<br>
     * InfReqID: tir92-130<br>
     * BusReqID: tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.Title<br>
     */
    protected String title;

    /**
     * Legislation description
     * <p>
     * Textual description of the legislation.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-131<br>
     * BusReqID: tbr92-015, tbr92-016<br>
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
     * InfReqID: tir92-132<br>
     * BusReqID: tbr92-015, tbr92-016<br>
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
     * InfReqID: tir92-133<br>
     * BusReqID: tbr92-015, tbr92-016<br>
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
     * InfReqID: tir92-134<br>
     * BusReqID: tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.LegislationReference.URI<br>
     */
    protected String URI;

    public LegislationReference(String title, String description, String jurisdictionLevelCode, String article, String URI) {
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
