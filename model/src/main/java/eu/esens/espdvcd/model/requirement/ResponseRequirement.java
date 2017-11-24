package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Criterion requirement
 *
 * Requirement to fulfill an specific criterion.
 *
 * Extends RequestRequirement by response and evidences.
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class ResponseRequirement extends RequestRequirement {

    private static final long serialVersionUID = 2750116567195274279L;

    /**
     * Criterion requirement response
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response<br>
     */
    private Response response;


    /**
     * Evidence
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.DescriptionResponse.Evidence<br>
     */
    private List<Evidence> evidences;

    public ResponseRequirement(String ID, ResponseTypeEnum responseDataType, String description) {
        super(ID, responseDataType, description);
    }

    public ResponseRequirement(String ID, ResponseTypeEnum responseDataType, String description, DescriptionResponse response, List<Evidence> evidences) {
        super(ID, responseDataType, description);
        this.response = response;
        this.evidences = evidences;
    }

    
    @Override
    public Response getResponse() {
        return this.response;
    }
 
    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public List<Evidence> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        return evidences;
    }

    @Override
    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }
}
