package eu.esens.espdvcd.model;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class LegislationReference {

    private String title;
    private String description;
    private String jurisdictionLevelCode;
    private String article;
    private String URI;

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
