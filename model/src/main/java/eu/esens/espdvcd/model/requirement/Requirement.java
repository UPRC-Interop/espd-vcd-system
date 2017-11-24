package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Criterion requirement interface
 *
 * Requirement to fulfill an specific criterion.
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */

public interface Requirement extends Serializable {
    String getID();

    void setID(String ID);

    ResponseTypeEnum getResponseDataType();

    void setResponseDataType(ResponseTypeEnum responseDataType);

    String getDescription();

    void setDescription(String description);

    Response getResponse();

    void setResponse(Response response);

    List<Evidence> getEvidences();

    void setEvidences(List<Evidence> evidences);
}
