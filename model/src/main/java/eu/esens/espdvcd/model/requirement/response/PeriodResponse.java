package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Period response
 *
 * Description response for defining a period.
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class PeriodResponse extends DescriptionResponse implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /*
     * Criterion response description - Criterion fulfillment period
     * <p>
     * Description of the period that is declared to fulfil this criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-168<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path:
     * ccv:Criterion.RequirementGroup.Requirement.Response.Description<br>
     */


    public PeriodResponse() {
    }

    public PeriodResponse(String description) {
        this.description = description;
    }

}
