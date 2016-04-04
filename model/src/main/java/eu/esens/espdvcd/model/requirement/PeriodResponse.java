package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class PeriodResponse extends DescriptionResponse implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;


    public PeriodResponse() {
    }

    public PeriodResponse(String description) {
        this.description = description;
    }

}
