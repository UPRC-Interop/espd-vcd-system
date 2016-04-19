package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class AmountResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion fulfillment amount
     * <p>
     * Declared amount that fulfills this criterion.
     * <p>
     * Data type: Amount<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-162<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Amount<br>
     */
    private float amount;
    private String currency;

    
    public AmountResponse() {
    }
    
    public AmountResponse(float amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public float getAmount() {
        return amount;
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
