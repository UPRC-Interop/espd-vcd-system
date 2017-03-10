package eu.esens.espdvcd.model.retriever.interfaces;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisAmountType {
    
    void setValue(String value);
    
    String getValue();
    
    void setCurrencyID(String currencyID);
    
    String getCurrencyID();
    
    void setCurrencyCodeListVersionID(String currencyCodeListVersionID);
    
    String getCurrencyCodeListVersionID();
    
}
