package eu.esens.espdvcd.model.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.*;

/**
 * Criterion requirement
 * <p>
 * Requirement to fulfill an specific criterion.
 * <p>
 * Extends RequestRequirement by response and evidences.
 * <p>
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class ResponseRequirement extends RequestRequirement {

    private static final long serialVersionUID = 2750116567195274279L;

    /**
     * Criterion requirement response
     * <p>
     * <p>
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response<br>
     */
    private Response response;

    public ResponseRequirement(@JsonProperty("ID") String ID,
                               @JsonProperty("responseDataType") ResponseTypeEnum responseDataType,
                               @JsonProperty("description") String description) {
        super(ID, responseDataType, description);
    }

    public ResponseRequirement(@JsonProperty("ID") String ID,
                               @JsonProperty("typeCode") CriterionElementTypeEnum typeCode,
                               @JsonProperty("responseDataType") ResponseTypeEnum responseDataType,
                               @JsonProperty("description") String description) {
        super(ID, typeCode, responseDataType, description);
    }

    @Override
    public Response getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

}
