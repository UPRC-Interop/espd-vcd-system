package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;


/**
 * Evidence URL response
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceURLResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Evidence URL
     * <p>
     * (not specified in domain vocabulary)
     * <p>
     * Data type: <br>
     * Cardinality: <br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
     */
    private String evidenceURL;

    public EvidenceURLResponse() {
    }

    public EvidenceURLResponse(String evidenceURL) {
        this.evidenceURL = evidenceURL;
    }

    public String getEvidenceURL() {
        return evidenceURL;
    }

    public void setEvidenceURL(String evidenceURL) {
        this.evidenceURL = evidenceURL;
    }
}
