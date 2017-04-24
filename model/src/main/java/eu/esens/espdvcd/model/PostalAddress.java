package eu.esens.espdvcd.model;

import javax.validation.constraints.NotNull;

/**
 * Postal Address
 *
 * FIXME: add PostBox field.
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
     *
     * Usage for Economic Operator:
     * InfReqID: tir92-041<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party. PostalAddress.StreetName<br>
     *
     * Usage for Contracting Authority:
     * InfReqID: tir92-012<br>
     * BusReqID: tbr92-012<br>
     * UBL syntax path: cac: ContractingParty. Party. PostalAddress.StreetName<br>
     *
     * Usage for Economic Operator Representative Natural Person:
     * InfReqID: tir92-058<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress.StreetName<br>
     *
     * Usage for Evidence Issuer (FIXME: not used anymore)
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
     *
     * Usage for Economic Operator:
     * InfReqID: tir92-043<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty. Party. PostalAddress.CityName<br>
     *
     * Usage for Contracting Authority:
     * InfReqID: tir92-013<br>
     * BusReqID: tbr92-012<br>
     * UBL syntax path: cac: ContractingParty. Party. PostalAddress.CityName<br>
     *
     * Usage for Economic Operator Representative Natural Person:
     * InfReqID: tir92-060<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress.CityName<br>
     *
     * Usage for Evidence Issuer (FIXME: not used anymore)
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
     *
     * Usage for Economic Operator:
     * InfReqID: tir92-044<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty. Party. PostalAddress.PostalZone<br>
     *
     * Usage for Contracting Authority:
     * InfReqID: tir92-014<br>
     * BusReqID: tbr92-012<br>
     * UBL syntax path: cac: ContractingParty. Party. PostalAddress.PostalZone<br>
     *
     * Usage for Economic Operator Representative Natural Person:
     * InfReqID: tir92-061<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress.PostalZone<br>
     *
     * Usage for Evidence Issuer (FIXME: not used anymore)
     * InfReqID: tir92-148<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.PostalZone<br>
     */
    private String postCode;


    /**
     * FIXME: not used according to specification
     *
     * Country subdivision
     * <p>
     * The subdivision of a country such as region, county, state, province etc.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     *
     * Usage for Economic Operator:
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
     *
     * Usage for Contracting Authority:
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
     *
     * Usage for Economic Operator Representative Natural Person:
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: <br>
     *
     * Usage for Evidence Issuer (FIXME: not used anymore)
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
     * Cardinality: 1..1<br>
     *
     * Usage for Economic Operator:
     * InfReqID: tir92-046<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty. Party. PostalAddress.Country.IdentificationCode<br>
     *
     * Usage for Contracting Authority:
     * InfReqID: tir92-016<br>
     * BusReqID: tbr92-011<br>
     * UBL syntax path: cac: ContractingParty. Party. PostalAddress.Country.IdentificationCode<br>
     *
     * Usage for Economic Operator Representative Natural Person:
     * InfReqID: tir92-063<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress.Country.IdentificationCode<br>
     *
     * Usage for Evidence Issuer (FIXME: not used anymore)
     * InfReqID:  tir92-150<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress.IdentificationCode<br>
     */
    @NotNull
    private String countryCode;

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
