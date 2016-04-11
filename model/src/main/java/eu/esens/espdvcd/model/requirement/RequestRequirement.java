package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.Evidence;
import eu.esens.espdvcd.model.requirement.response.Responses;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */
public class RequestRequirement implements Requirement {

    private static final long serialVersionUID = 528517963577425517L;
    private String ID;
    private Responses.Type responseDataType;
    private String description;

    public RequestRequirement(String ID, Responses.Type responseDataType, String description) {
        this.ID = ID;
        this.responseDataType = responseDataType;
        this.description = description;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Response getResponse() {
        throw new UnsupportedOperationException("Not supported in ESPD request.");
    }

    @Override
    public void setResponse(Response response) {
        throw new UnsupportedOperationException("Not supported in ESPD request.");
    }

    @Override
    public List<Evidence> getEvidences() {
        throw new UnsupportedOperationException("Not supported in ESPD request.");
    }

    @Override
    public void setEvidences(List<Evidence> evidences) {
        throw new UnsupportedOperationException("Not supported in ESPD request.");
    }

    @Override
    public void setResponseDataType(Responses.Type responseDataType) {
        this.responseDataType = responseDataType;
    }

    @Override
    public Responses.Type getResponseDataType() {
      return this.responseDataType;
    }
}