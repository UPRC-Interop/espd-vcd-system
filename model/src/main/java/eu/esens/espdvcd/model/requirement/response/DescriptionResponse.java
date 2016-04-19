package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

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
    
    protected String description;


    public DescriptionResponse() {
    }

    public DescriptionResponse(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
