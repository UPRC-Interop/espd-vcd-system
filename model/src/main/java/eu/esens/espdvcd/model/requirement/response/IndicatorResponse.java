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
     * Indicator
     * <p>
     * (generic boolean indicator)
     * <p>
     * Data type: <br>
     * Cardinality: <br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
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
