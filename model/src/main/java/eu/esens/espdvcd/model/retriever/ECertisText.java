package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"languageID", "value"} )
public class ECertisText implements IECertisText {
    
    private String languageID;
    private String value;
    
    @Override
    @JsonProperty("languageID")
    public String getLanguageID() {
        return languageID;
    }

    @Override
    public void setLanguageID(String languageID) {
        this.languageID = languageID;
    }

    @Override
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
}
