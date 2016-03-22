package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class CountryCodeResponse extends Response implements Serializable {

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
    private String description;

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

    public CountryCodeResponse(String description) {
        this.description = description;
    }

    public CountryCodeResponse(String description, String countryCode) {
        this.description = description;
        this.countryCode = countryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
