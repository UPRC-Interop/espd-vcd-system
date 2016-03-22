package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class ReverseIndicatorResponse extends Response implements Serializable {

    private static final long serialVersionUID = 1893521229468775082L;

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


    private boolean reverseIndicator;

    public ReverseIndicatorResponse() {
    }

    public ReverseIndicatorResponse(String description) {
        this.description = description;
    }

    public ReverseIndicatorResponse(String description, boolean reverseIndicator) {
        this.description = description;
        this.reverseIndicator = reverseIndicator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReverseIndicator() {
        return reverseIndicator;
    }

    public void setReverseIndicator(boolean reverseIndicator) {
        this.reverseIndicator = reverseIndicator;
    }
}
