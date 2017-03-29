package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder({"value", "currencyID", "currencyCodeListVersionID"})
public class ECertisAmountImpl implements ECertisAmount {
        
    private String value;
    private String currencyID;
    private String currencyCodeListVersionID;
    
    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    @Override
    @JsonProperty("currencyID")
    public String getCurrencyID() {
        return currencyID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setCurrencyCodeListVersionID(String currencyCodeListVersionID) {
        this.currencyCodeListVersionID = currencyCodeListVersionID;
    }

    @Override
    @JsonProperty("currencyCodeListVersionID")
    public String getCurrencyCodeListVersionID() {
        return currencyCodeListVersionID;
    }
        
}
