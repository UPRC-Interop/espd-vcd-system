package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 22/03/2016.
 */
public class IndicatorResponse extends Response implements Serializable {

    private static final long serialVersionUID = 776800490020376197L;
 

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
