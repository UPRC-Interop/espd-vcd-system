package eu.esens.espdvcd.model.retriever;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisAmount {
    
    void setValue(String value);
    
    String getValue();
    
    void setCurrencyID(String currencyID);
    
    String getCurrencyID();
    
    void setCurrencyCodeListVersionID(String currencyCodeListVersionID);
    
    String getCurrencyCodeListVersionID();
    
}
