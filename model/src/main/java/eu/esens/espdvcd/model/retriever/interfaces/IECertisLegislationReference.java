package eu.esens.espdvcd.model.retriever.interfaces;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisLegislationReference {

    IECertisText getTitle();

    void setTitle(IECertisText title);

    IECertisText getDescription();

    void setDescription(IECertisText description);

    String getJurisdictionLevelCode();

    void setJurisdictionLevelCode(String JurisdictionLevelCode);

    IECertisText getArticle();

    void setArticle(IECertisText article);

    IECertisURI getURI();
    
    void setURI(IECertisURI URI);
    
 }
