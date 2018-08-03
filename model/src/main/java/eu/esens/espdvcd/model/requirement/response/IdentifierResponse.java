package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

public class IdentifierResponse extends Response implements Serializable {

    private static final long serialVersionUID = 776803495520376887L;

    /**
     * Criterion fulfillment response identifier
     * <p>
     * An identifier used as a reply to the criterion requirement response.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-530<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Identifier<br>
     */
    private String identifier;

    public IdentifierResponse() {

    }

    public IdentifierResponse(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
