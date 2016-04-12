package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceURLCodeResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    private String evidenceURLCode;

    public EvidenceURLCodeResponse() {
    }

    public EvidenceURLCodeResponse(String evidenceURLCode) {
        this.evidenceURLCode = evidenceURLCode;
    }

    public String getEvidenceURLCode() {
        return evidenceURLCode;
    }

    public void setEvidenceURLCode(String evidenceURLCode) {
        this.evidenceURLCode = evidenceURLCode;
    }
}
