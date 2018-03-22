package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Quantity year response
 *
 * (part of Period declaration fulfilment)
 *
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class QuantityYearResponse extends Response implements Serializable {

    private static final long serialVersionUID = -9178109536250269929L;

    /**
     * Criterion fulfillment date
     * <p>
     * Declared date where this criterion was fulfilled.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-166<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Date<br>
     */
    int year;
    
    public QuantityYearResponse() {
    }


    public QuantityYearResponse(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
