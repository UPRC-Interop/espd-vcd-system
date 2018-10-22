package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.util.List;

public class LotsIdentifierResponse extends Response implements Serializable {

    private List<String> lots;

    public LotsIdentifierResponse() {
    }

    public LotsIdentifierResponse(List<String> lots) {
        this.lots = lots;
    }

    public List<String> getLots() {
        return lots;
    }

    public void setLots(List<String> lots) {
        this.lots = lots;
    }
}
