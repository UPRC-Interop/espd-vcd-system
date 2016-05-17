package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Country code response
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class CountryCodeResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;

    /**
     * Country code
     * <p>
     * A code that identifies the country. The lists of valid countries are registered with the
     * ISO 3166-1 Maintenance agency, "Codes for the representation of names of countries and
     * their subdivisions". It is recommended to use the alpha-2 representation.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-150<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.IdentificationCode<br>
     */
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
