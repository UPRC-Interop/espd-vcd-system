package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceURLResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion response description
     * <p>
     * A textual description of a criterion response that describes how an
     * economic operators fulfills an specific criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-159<br>
     * BusReqID: tbr92-018, tbr92-007, tbr92-005<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.DescriptionResponse.Description<br>
     */
    private String description;

    private String evidenceURL;

    public EvidenceURLResponse() {
    }

    public EvidenceURLResponse(String description) {
        this.description = description;
    }

    public EvidenceURLResponse(String description, String evidenceURL) {
        this.description = description;
        this.evidenceURL = evidenceURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvidenceURL() {
        return evidenceURL;
    }

    public void setEvidenceURL(String evidenceURL) {
        this.evidenceURL = evidenceURL;
    }
}
