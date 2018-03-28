package eu.esens.espdvcd.model.requirement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;
import java.io.Serializable;

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

}
