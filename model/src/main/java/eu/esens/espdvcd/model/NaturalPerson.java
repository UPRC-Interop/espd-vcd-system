package eu.esens.espdvcd.model;

import java.util.GregorianCalendar;

/**
 * 	NaturalPerson
 *
 * 	Information about individuals who in one way or the other represent the economic operator.
 *
 * 	BusReqID: tbr92-018
 *
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class NaturalPerson {

    /**
     * Natural person name
     * <p>
     * Name of the natural person.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-055<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.Person.FamilyName<br>
     */
    private String name;


    /**
     * Natural person identifier
     * <p>
     * Identifier of the natural person.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-056<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.Person.ID<br>
     */
    private String ID;


    /**
     * Natural person role
     * <p>
     * Role of the natural person.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-057<br>
     * BusReqID: tbr92-010<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.RepresentativeNaturalPerson.NaturalPersonRoleCode<br>
     */
    private String role;


    /**
     * Natural person birth place
     * <p>
     * Place of birth of the natural person.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-065<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.Person.BirthplaceName<br>
     */
    private String birthPlace;


    /**
     * Natural person birth date
     * <p>
     * Date of birth of the natural person.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-066<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.Person.BirthDate<br>
     */
    private GregorianCalendar birthDate;


    /**
     * Natural person registration country
     * <p>
     * Country of registration of the natural person.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-064<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.RepresentativeNaturalPerson.NaturalPersonRegistrationCountryCode<br>
     */
    private String registrationCountry;


    /**
     * Power of attorney
     * <p>
     * Power of attorney of the natural person.
     * Remark: No data type has been specified in domain vocabulary.
     * Using a String according to implementation guidelines. FIXME
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-067<br>
     * BusReqID: tbr92-010<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description<br>
     */
    private String powerOfAttorney;


    /**
     * Postal address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress<br>
     */
    private PostalAddress postalAddress;


    public NaturalPerson(String name, String ID, String role, String birthPlace, GregorianCalendar birthDate, String registrationCountry, String powerOfAttorney, PostalAddress postalAddress) {
        this.name = name;
        this.ID = ID;
        this.role = role;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.registrationCountry = registrationCountry;
        this.powerOfAttorney = powerOfAttorney;
        this.postalAddress = postalAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public GregorianCalendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(GregorianCalendar birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegistrationCountry() {
        return registrationCountry;
    }

    public void setRegistrationCountry(String registrationCountry) {
        this.registrationCountry = registrationCountry;
    }

    public String getPowerOfAttorney() {
        return powerOfAttorney;
    }

    public void setPowerOfAttorney(String powerOfAttorney) {
        this.powerOfAttorney = powerOfAttorney;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }
}
