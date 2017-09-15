package eu.esens.espdvcd.model.retriever;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisLegislationReference {

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    String getJurisdictionLevelCode();

    void setJurisdictionLevelCode(String JurisdictionLevelCode);

    String getArticle();

    void setArticle(String article);

    String getURI();

    void setURI(String URI);

}
