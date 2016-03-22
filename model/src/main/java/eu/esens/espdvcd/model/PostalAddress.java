package eu.esens.espdvcd.model;

/**
 * Postal Address
 *
 * Address information.
 *
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class PostalAddress {

    /**
     * Address line 1
     * <p>
     * The main address line in an address. Usually the street name and number or post office box.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-145<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.StreetName<br>
     */
    private String addressLine1;


    /**
     * City
     * <p>
     * The common name of a city where the address is located.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-147<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.CityName<br>
     */
    private String city;


    /**
     * Post code
     * <p>
     * The identifier for an addressable group of properties according to the relevant postal service, such as a ZIP code or Post Code.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-148<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.PostalZone<br>
     */
    private String postCode;


    /**
     * Country subdivision
     * <p>
     * The subdivision of a country such as region, county, state, province etc.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-149<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.CountrySubentity<br>
     */
    private String countrySubdivision;


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

    public PostalAddress(String addressLine1, String city, String postCode, String countrySubdivision, String countryCode) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.postCode = postCode;
        this.countrySubdivision = countrySubdivision;
        this.countryCode = countryCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountrySubdivision() {
        return countrySubdivision;
    }

    public void setCountrySubdivision(String countrySubdivision) {
        this.countrySubdivision = countrySubdivision;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
