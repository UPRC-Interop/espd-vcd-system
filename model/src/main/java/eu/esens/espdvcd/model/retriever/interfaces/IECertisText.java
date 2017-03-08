package eu.esens.espdvcd.model.retriever.interfaces;

/**
 *
 * @author Konstantinos Raptis
 */

public interface IECertisText {
    
    String getLanguageID();
    
    void setLanguageID(String languageID);

    String getValue();
    
    void setValue(String value);
    
}
