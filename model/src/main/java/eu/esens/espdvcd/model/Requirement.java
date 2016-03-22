package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public interface Requirement extends Serializable {
    String getID();

    void setID(String ID);

    String getResponseDataType();

    void setResponseDataType(String responseDataType);

    String getDescription();

    void setDescription(String description);

    RequirementResponse getResponse();

    void setResponse(RequirementResponse response);

    List<Evidence> getEvidences();

    void setEvidences(List<Evidence> evidences);
}
