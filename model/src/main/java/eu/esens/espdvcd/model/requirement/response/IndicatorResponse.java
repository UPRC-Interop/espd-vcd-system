package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

/**
 * Indicator response
 *
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class IndicatorResponse extends Response implements Serializable {

    private static final long serialVersionUID = 776800490020376197L;

    /**
     * Criterion fulfillment indicator
     * <p>
     * Indicates whether the economic operator states that he fulfills the specific criterion (true) or not (false).
     * <p>
     * Data type: Indicator<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-160<br>
     * BusReqID: tbr92-018, tbr92-007, tbr92-005, tbr92-006<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Indicator<br>
     */
    private boolean indicator;

    public IndicatorResponse() {
    }

    public IndicatorResponse(boolean indicator) {
        this.indicator = indicator;
    }

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }
}
