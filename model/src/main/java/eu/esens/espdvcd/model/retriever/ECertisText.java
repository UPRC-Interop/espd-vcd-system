package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

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
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setLanguageID(String languageID) {
        this.languageID = languageID;
    }

    @Override
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setValue(String value) {
        this.value = value;
    }
    
}
