package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class QuantityIntegerResponse extends Response implements Serializable {

    private static final long serialVersionUID = 8321476080155936015L;

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

    public QuantityIntegerResponse(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
