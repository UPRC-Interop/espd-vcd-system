package eu.esens.espdvcd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class ResponseRequirement extends RequestRequirement {



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
    private RequirementResponse response;


    /**
     * Evidence
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Evidence<br>
     */
    private List<Evidence> evidences;

    public ResponseRequirement(String ID, String responseDataType, String description) {
        super(ID, responseDataType, description);
    }

    public ResponseRequirement(String ID, String responseDataType, String description, RequirementResponse response, List<Evidence> evidences) {
        super(ID, responseDataType, description);
        this.response = response;
        this.evidences = evidences;
    }

    @Override
    public RequirementResponse getResponse() {
        return response;
    }

    @Override
    public void setResponse(RequirementResponse response) {
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
