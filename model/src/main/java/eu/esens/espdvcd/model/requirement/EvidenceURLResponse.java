package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;


/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceURLResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

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
