package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class QuantityYearResponse extends Response implements Serializable {

    private static final long serialVersionUID = -9178109536250269929L;

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

    private int year;

    public QuantityYearResponse() {
    }

    public QuantityYearResponse(String description) {
        this.description = description;
    }

    public QuantityYearResponse(String description, int year) {
        this.description = description;
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
