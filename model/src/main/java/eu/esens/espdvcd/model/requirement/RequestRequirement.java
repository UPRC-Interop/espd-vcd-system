package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.Evidence;
import eu.esens.espdvcd.model.requirement.response.ResponseFactory;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Criterion requirement
 *
 * Requirement to fulfill an specific criterion.
 *
 * Created by ixuz on 2/24/16.
 */
public class RequestRequirement implements Requirement {

    private static final long serialVersionUID = 528517963577425517L;





    /**
     * Criterion requirement identifier
     * <p>
     * Identifier of the requirement that fulfills an specific criterion.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir070-080<br>
     * BusReqID: tbr70-013, tbr70-004<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.ID<br>
     */
    @NotNull
    private String ID;

    /**
     * Criterion requirement description
     * <p>
     * Description of the requirement that fulfills an specific criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir070-081<br>
     * BusReqID: tbr70-013, tbr70-004<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Description<br>
     */
    @NotNull
    private String description;

    /**
     * Criterion response indicator
     * <p>
     * An indicator which points to the expected data type of the response.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: tbr92-015, tbr92-016, tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.ResponseDataType<br>
     */
    private ResponseTypeEnum responseDataType;


    public RequestRequirement(String ID, ResponseTypeEnum responseDataType, String description) {
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
    public void setResponseDataType(ResponseTypeEnum responseDataType) {
        this.responseDataType = responseDataType;
    }

    @Override
    public ResponseTypeEnum getResponseDataType() {
      return this.responseDataType;
    }
}