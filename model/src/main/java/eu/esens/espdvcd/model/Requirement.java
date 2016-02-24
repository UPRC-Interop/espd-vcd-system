package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 * Created by ixuz on 2/24/16.
 */
public class Requirement implements Serializable {
    private String ID;
    private String responseDataType;
    private String description;

    public Requirement(String ID, String responseDataType, String description) {
        this.ID = ID;
        this.responseDataType = responseDataType;
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getResponseDataType() {
        return responseDataType;
    }

    public void setResponseDataType(String responseDataType) {
        this.responseDataType = responseDataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}