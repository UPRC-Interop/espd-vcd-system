package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisID;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisID implements IECertisID {

    private String ID;

    public ECertisID() {
    }

    public ECertisID(String ID) {
        this.ID = ID;
    }
    
    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

}
