package eu.esens.espdvcd.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;

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
     * Representative natural person first name
     * <p>
     * Name of the natural person.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-055<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description.AgentParty.Person.FirstName<br>
     */
    @NotNull
    private String firstName;
    
    /**
     * Representative natural person family name
     * <p>
     * Family Name of the natural person.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-055<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description.AgentParty.Person.FamilyName<br>
     */
    @NotNull
    private String familyName;


    /**
     * Representative natural person role description
     * <p>
     * The short description for the role of the economic operators representative.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-065<br>
     * BusReqID: tbr92-010<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.NaturalPersonRoleDescription<br>
     */
    private String role;


    /**
     * Representative natural person birth place
     * <p>
     * Place of birth of the natural person.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-065<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description.AgentParty.Person.BirthplaceName<br>
     */
    private String birthPlace;


    /**
     * Representative natural person birth date
     * <p>
     * Date of birth of the natural person.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-066<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description.AgentParty.Person.BirthDate<br>
     */
    private LocalDate birthDate;

    /**
     * Postal address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 1..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress<br>
     */
    @NotNull
    private PostalAddress postalAddress;

    /**
     * Representative natural person telephone number and email adress
     * <p>
     * A phone number and e-mail address for the contact point.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-064, tir92-067<br>
     * BusReqID: tbr92-009<br>
     * UBL syntax path: espd-cac: EconomicOperatorParty.Party.Person.ResidenceAddress<br>
     */
    private ContactingDetails contactDetails;
    
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public ContactingDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactingDetails contactDetails) {
        this.contactDetails = contactDetails;
    }
    
}
