package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class PercentageResponse extends Response implements Serializable {

    private static final long serialVersionUID = -7417383925707504848L;

    /**
     * Criterion fulfillment percenatge
     * <p>
     * Declared percentage that fulfills this criterion.
     * <p>
     * Data type: Percentage<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-164<br>
     * BusReqID: tbr92-018, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Percent<br>
     */
    private float percentage;

    public PercentageResponse() {
    }

    public PercentageResponse(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
