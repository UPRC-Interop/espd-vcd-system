package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;

import java.io.Serializable;

/**
 * Criterion requirement interface
 * <p>
 * Requirement to fulfill an specific criterion.
 * <p>
 * Created by Ulf Lotzmann on 21/03/2016.
 */

public interface Requirement extends Serializable {
    String getID();

    void setID(String ID);

    String getUUID();

    void setUUID(String UUID);

    /**
     * In edm version 2 this is mapped to: cbc:ValueDataTypeCode
     *
     * @return
     */
    ResponseTypeEnum getResponseDataType();

    void setResponseDataType(ResponseTypeEnum responseDataType);

    /**
     * Possible types are 'CAPTION , REQUIREMENT, QUESTION'
     * <p>
     * The Regulated ESPD documents do not specify REQUIREMENTS,
     * only QUESTIONS. The SELF-CONTAINED version does.
     *
     * @return
     */
    CriterionElementTypeEnum getTypeCode();

    void setTypeCode(CriterionElementTypeEnum typeCode);

    String getDescription();

    void setDescription(String description);

    Response getResponse();

    void setResponse(Response response);

    void setMandatory(boolean mandatory);

    void setMultiple(boolean multiple);

    boolean isMandatory();

    boolean isMultiple();

}
