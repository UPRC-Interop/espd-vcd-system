package eu.esens.espdvcd.model.requirement.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Evidence identifier response
 *
 * Response for defining an evidence.
 *
 */
public class EvidenceIdentifierResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1734098673888993636L;

    private String evidenceSuppliedId;

    public EvidenceIdentifierResponse() {

    }

    public EvidenceIdentifierResponse(String evidenceSuppliedId) {
        this.evidenceSuppliedId = evidenceSuppliedId;
    }

    public String getEvidenceSuppliedId() {
        return evidenceSuppliedId;
    }

    public void setEvidenceSuppliedId(String evidenceSuppliedId) {
        this.evidenceSuppliedId = evidenceSuppliedId;
    }

}
