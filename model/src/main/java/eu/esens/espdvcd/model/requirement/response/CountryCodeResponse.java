package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * Country code response
 *
 * Is used in version 1.0.2 for country code and code to access the online evidence.
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class CountryCodeResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Criterion fulfillment code
     * <p>
     * Event declared to fulfil this criterion, expressed as a code.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-167<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Code<br>
     */
    @NotNull
    private String countryCode;

    public CountryCodeResponse() {
    }

    public CountryCodeResponse(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
