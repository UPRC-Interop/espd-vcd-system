package eu.esens.espdvcd.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Economic operator
 *
 * Any natural or legal person or public entity or group of such persons and/or
 * entities, including any temporary association of undertakings, which offers
 * the execution of works and/or a work, the supply of products or the provision
 * of services on the market. Information about the party submitting the
 * qualification.
 *
 * BusReqID: tbr92-017, tbr92-010, tbr92-028, tbr92-029
 *
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EODetails {

    /**
     * Economic operator identifier
     * <p>
     * An identifier that identifies the economic operator, such as a legal
     * registration identifier.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-030<br>
     * BusReqID: tbr92-001<br>
     * UBL syntax path:
     * espd-cac:EconomicOperatorParty.Party.PartyIdentification<br>
     */
    @NotNull
    private String ID;

    /**
     * Economic operator electronic address identifier
     * <p>
     * Electronic address of the economic operator.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-031<br>
     * BusReqID: tbr92-001<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.EndpointID<br>
     */
    private String electronicAddressID;

    /**
     * Economic operator website
     * <p>
     * The website of the economic operator.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-316<br>
     * BusReqID: tbr041-002<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.WebsiteURIID<br>
     */
    private String webSiteURI;

    /**
     * FIXME: not used
     *
     * Economic operator registration country code
     * <p>
     * The registration country code of the economic operator.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-032<br>
     * BusReqID: tbr92-003<br>
     * UBL syntax path:
     * espd-cac:EconomicOperatorParty.Party.PartyLegalEntity.RegistrationAddress.Country.IdentificationCode<br>
     */
    private String registrationCountryCode;

    /**
     * Economic operator name
     * <p>
     * The name of the economic operator.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-033<br>
     * BusReqID: tbr92-001<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.PartyName.Name<br>
     */
    @NotNull
    private String name;

    /**
     * Economic operator role
     * <p>
     * The role of the economic operator when bidding from a consortium. (main
     * contractor, subcontractor , additional)
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-034<br>
     * BusReqID: tbr92-008<br>
     * UBL syntax path:
     * espd-cac:EconomicOperatorParty.EconomicOperatorRoleCode<br>
     */
    private String role;

    /**
     * SME indicator
     * <p>
     * Indicates whether the economic operator can be categorized as an SME
     * (true) or not. According to the EC, SME are enterprises with less than
     * 250 employees, a turnover less than EUR 50 m and a balance sheet total
     * less than EUR 43 m.
     * <p>
     * Data type: Indicator<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-035<br>
     * BusReqID: tbr92-004<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.SMEIndicator<br>
     */
    private boolean smeIndicator;

    /**
     * FIXME: not used
     *
     * National database URI
     * <p>
     * Unrestricted and full direct access to tools and devices used for
     * electronic communication is possible at this URL.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-036<br>
     * BusReqID: tbr92-006, tbr92-007<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.NationalDatabaseURIID<br>
     */
    private String nationalDatabaseURI;

    /**
     * FIXME: not used
     *
     * National database access credentials
     * <p>
     * Unrestricted and full direct access to tools and devices used for
     * electronic communication is possible at this URL.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-037<br>
     * BusReqID: tbr92-006, tbr92-007<br>
     * UBL syntax path:
     * espd-cac:EconomicOperatorParty.NationalDatabaseAccessCredentials<br>
     */
    private String nationalDatabaseAccessCredentials;

    /**
     * Postal address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 1..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.PostalAddress<br>
     */
    @NotNull
    private PostalAddress postalAddress;

    /**
     * Contacting details
     * <p>
     * Used to provide contacting information for a party in general or a
     * person.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party.Contact<br>
     */
    private ContactingDetails contactingDetails;

    /**
     * Representative natural person
     * <p>
     * Information about individuals who in one way or the other represent the
     * economic operator.
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path:
     * espd-cac: EconomicOperatorParty.RepresentativeNaturalPerson.PowerOfAttorney.Description.AgentParty.Person<br>
     */
    private List<NaturalPerson> naturalPersons;
    
        /**
     * Lot reference
     * <p>
     * An identifier for the lot.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1 (1..n)<br>
     * InfReqID: <br>
     * BusReqID: tbr92-014<br>
     * UBL syntax path: cac:ProcurementProjectLot.ID<br>
     */
    @NotNull
    private String procurementProjectLot;

    public String getProcurementProjectLot() {
        return procurementProjectLot;
    }

    public void setProcurementProjectLot(String procurementProjectLot) {
        this.procurementProjectLot = procurementProjectLot;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getElectronicAddressID() {
        return electronicAddressID;
    }

    public void setElectronicAddressID(String electronicAddressID) {
        this.electronicAddressID = electronicAddressID;
    }

    public String getRegistrationCountryCode() {
        return registrationCountryCode;
    }

    public void setRegistrationCountryCode(String registrationCountryCode) {
        this.registrationCountryCode = registrationCountryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isSmeIndicator() {
        return smeIndicator;
    }

    public void setSmeIndicator(boolean smeIndicator) {
        this.smeIndicator = smeIndicator;
    }

    public String getNationalDatabaseURI() {
        return nationalDatabaseURI;
    }

    public void setNationalDatabaseURI(String nationalDatabaseURI) {
        this.nationalDatabaseURI = nationalDatabaseURI;
    }

    public String getNationalDatabaseAccessCredentials() {
        return nationalDatabaseAccessCredentials;
    }

    public void setNationalDatabaseAccessCredentials(String nationalDatabaseAccessCredentials) {
        this.nationalDatabaseAccessCredentials = nationalDatabaseAccessCredentials;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public List<NaturalPerson> getNaturalPersons() {
        if (naturalPersons == null) {
            naturalPersons = new ArrayList<>();
        }
        return naturalPersons;
    }

    public void setNaturalPersons(List<NaturalPerson> naturalPersons) {
        this.naturalPersons = naturalPersons;
    }

    public ContactingDetails getContactingDetails() {
        return contactingDetails;
    }

    public void setContactingDetails(ContactingDetails contactingDetails) {
        this.contactingDetails = contactingDetails;
    }

    public String getWebSiteURI() {
        return webSiteURI;
    }

    public void setWebSiteURI(String webSiteURI) {
        this.webSiteURI = webSiteURI;
    }
}
