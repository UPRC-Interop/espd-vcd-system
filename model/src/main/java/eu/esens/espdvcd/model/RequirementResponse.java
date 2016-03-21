package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class RequirementResponse implements Serializable {

    /**
     * Criterion response identifier
     * <p>
     * A language-independent token, e.g., a number, that allows to identify a
     * criterion response uniquely as well as allows to reference the criterion
     * response in other documents. A criterion response describes how an
     * economic operators fulfills an specific criterion.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-158<br>
     * BusReqID: tbr92-018, tbr92-007, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.ID<br>
     */
    private String ID;


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
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Description<br>
     */
    private String description;

    /**
     * Criterion fulfillment indicator
     * <p>
     * Indicates whether the economic operator states that he fulfills the specific criterion (true) or not (false).
     * <p>
     * Data type: Indicator<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-160<br>
     * BusReqID: tbr92-018, tbr92-007, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Indicator<br>
     */
    private boolean fulfillmentIndicator;


    /**
     * Period declaration fulfillment
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response<br>
     */
    private List<PeriodDeclarationFulfillment> periodDeclarationFulfillments;


    public RequirementResponse(String ID, String description, boolean fulfillmentIndicator) {
        this.ID = ID;
        this.description = description;
        this.fulfillmentIndicator = fulfillmentIndicator;
    }

    public RequirementResponse(String ID, String description, boolean fulfillmentIndicator, List<PeriodDeclarationFulfillment> periodDeclarationFulfillments) {
        this.ID = ID;
        this.description = description;
        this.fulfillmentIndicator = fulfillmentIndicator;
        this.periodDeclarationFulfillments = periodDeclarationFulfillments;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFulfillmentIndicator() {
        return fulfillmentIndicator;
    }

    public void setFulfillmentIndicator(boolean fulfillmentIndicator) {
        this.fulfillmentIndicator = fulfillmentIndicator;
    }

    public List<PeriodDeclarationFulfillment> getPeriodDeclarationFulfillments() {
        if (periodDeclarationFulfillments == null) {
            periodDeclarationFulfillments = new ArrayList<>();
        }
        return periodDeclarationFulfillments;
    }

    public void setPeriodDeclarationFulfillments(List<PeriodDeclarationFulfillment> periodDeclarationFulfillments) {
        this.periodDeclarationFulfillments = periodDeclarationFulfillments;
    }
}
