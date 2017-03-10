package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisAmountType;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder({"value", "currencyID", "currencyCodeListVersionID"})
public class ECertisAmountType implements IECertisAmountType {
        
    private String value;
    private String currencyID;
    private String currencyCodeListVersionID;
    
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @Override
    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    @Override
    @JsonProperty("currencyID")
    public String getCurrencyID() {
        return currencyID;
    }

    @Override
    public void setCurrencyCodeListVersionID(String currencyCodeListVersionID) {
        this.currencyCodeListVersionID = currencyCodeListVersionID;
    }

    @Override
    @JsonProperty("currencyCodeListVersionID")
    public String getCurrencyCodeListVersionID() {
        return currencyCodeListVersionID;
    }
        
}
