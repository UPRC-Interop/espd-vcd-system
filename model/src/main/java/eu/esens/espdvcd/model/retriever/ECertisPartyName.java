package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisPartyName;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisPartyName implements IECertisPartyName {

    @JsonDeserialize(as = ECertisText.class)
    private IECertisText name;
    
    @Override
    public void setName(IECertisText name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    public IECertisText getName() {
        return name;
    }
        
}
