package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class PercentageResponse extends Response implements Serializable {

    private static final long serialVersionUID = -7417383925707504848L;

    /**
     * Criterion response description
     * <p>
     * A textual description of a criterion response that describes how an
     * economic operators fulfills an specific criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-159<br>
     * BusReqID: tbr92-018, tbr92-007, tbr92-005<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.DescriptionResponse.Description<br>
     */
    private String description;

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

    public PercentageResponse(String description) {
        this.description = description;
    }

    public PercentageResponse(String description, float percentage) {
        this.description = description;
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
