package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.model.Evidence;
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

    Response getResponse();

    void setResponse(Response response);

    List<Evidence> getEvidences();

    void setEvidences(List<Evidence> evidences);
}
