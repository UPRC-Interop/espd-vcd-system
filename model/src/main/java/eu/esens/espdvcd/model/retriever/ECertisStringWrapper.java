package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisStringWrapper;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisStringWrapper implements IECertisStringWrapper {
    
    private String value;

    public ECertisStringWrapper() {
    }

    public ECertisStringWrapper(String value) {
        this.value = value;
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
