package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class QuantityIntegerResponse extends Response implements Serializable {

    private static final long serialVersionUID = 8321476080155936015L;

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
     * Criterion fulfillment quantity
     * <p>
     * UOM should be stated by using recommendation 20 v10 Declared quantity that fulfills this criterion.
     * <p>
     * Data type: Quantity<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-163<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Quantity<br>
     */
    private int quantity;

    public QuantityIntegerResponse() {
    }

    public QuantityIntegerResponse(String description) {
        this.description = description;
    }

    public QuantityIntegerResponse(String description, int quantity) {
        this.description = description;
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
