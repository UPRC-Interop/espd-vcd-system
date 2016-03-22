package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class DescriptionResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

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
     * Criterion fulfillment description
     * <p>
     * Textual description of the fulfilment of this criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-165<br>
     * BusReqID: tbr92-018, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Description<br>
     */
    private String fulfillmentDescription;

    public DescriptionResponse() {
    }

    public DescriptionResponse(String description) {
        this.description = description;
    }

    public DescriptionResponse(String description, String fulfillmentDescription) {
        this.description = description;
        this.fulfillmentDescription = fulfillmentDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFulfillmentDescription() {
        return fulfillmentDescription;
    }

    public void setFulfillmentDescription(String fulfillmentDescription) {
        this.fulfillmentDescription = fulfillmentDescription;
    }
}
