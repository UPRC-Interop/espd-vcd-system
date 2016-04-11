package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class DateResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion fulfillment date
     * <p>
     * Declared date where  this criterion was fulfilled.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-166<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Date<br>
     */
    private Date date;

    public DateResponse() {
    }

    public DateResponse(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
