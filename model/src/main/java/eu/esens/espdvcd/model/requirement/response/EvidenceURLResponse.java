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
     * External document URI
     * <p>
     * The Uniform Resource Identifier (URI) that identifies where the external document is located.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-151<br>
     * BusReqID: tbr92-017, tbr92-022, tbr92-006, tbr92-007<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.
     *                     Evidence.EvidenceDocumentReference.Attachment.ExternalReference.URI<br>
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
