package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

public class EOIdentifierResponse extends Response implements Serializable {
    private String EOIDType;
    private String ID;

    public EOIdentifierResponse(String EOIDType, String ID) {
        this.EOIDType = EOIDType;
        this.ID = ID;
    }

    public EOIdentifierResponse() {
    }

    public String getEOIDType() {
        return EOIDType;
    }

    public void setEOIDType(String EOIDType) {
        this.EOIDType = EOIDType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
