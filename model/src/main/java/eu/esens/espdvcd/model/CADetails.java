package eu.esens.espdvcd.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * Contracting authority and procurement procedure details
 *
 * The contracting authority or contracting entity who is buying supplies, services or public works using a
 * tendering procedure as described in the applicable directive (Directives 2014/24/EU, 2014/25/EU).
 *
 *
 */
public class CADetails implements Serializable{

    private static final long serialVersionUID = -2251052431953226768L;

    /**
     * Contracting body name
     * <p>
     * The name of the contracting body as it is registered.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-009<br>
     * BusReqID: tbr92-011<br>
     * UBL syntax path: cac:ContractingParty.Party.PartyName.Name<br>
     */
    @NotNull
    private String caOfficialName;

    /**
     * Notice name
     * <p>
     * Title of the Contract Notice
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.Attachment.ExternalReference.FileName<br>
     */
    private String procurementProcedureTitle;

    /**
     * Notice description
     * <p>
     * Short description of the Procurement Project
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.Attachment.ExternalReference.Description<br>
     */
    private String procurementProcedureDesc;

    /**
     * Reference number
     * <p>
     * An identifier that is specified by the buyer and used as a reference number for all documents in
     * the procurement process. It is also known as procurement project identifier, procurement reference number
     * or contract folder identifier. A reference to the procurement process to which this Qualification document
     * is delivered as a response.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: <br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cbc:ContractFolderID<br>
     */
    @NotNull
    private String procurementProcedureFileReferenceNo;



    /**
     * Notice number / identifier
     * <p>
     * The identifier of the Contract Notice published in TeD (the OJEU S number).<br>
     * For procurement projects above the threshold it is compulsory to specify the following
     * data about the Contract Notice published in TeD.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.ID<br>
     */
    private String procurementPublicationNumber;
   
    //This could be changed to something that comes from an enumeration
    /**
     * Country code
     * <p>
     * A code that identifies the country. The lists of valid countries are registered with the
     * ISO 3166-1 Maintenance agency, "Codes for the representation of names of countries and their
     * subdivisions". It is recommended to use the alpha-2 representation.
     * <p>
     * Data type: Code<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-016<br>
     * BusReqID: tbr92-011<br>
     * UBL syntax path: cac:ContractingParty.Party.PostalAddress.Country.IdentificationCode<br>
     */
    @NotNull
    private String caCountry;

    /**
     * Contracting body electronic address identifier
     * <p>
     * Electronic address of the contracting body.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-039<br>
     * BusReqID: tbr70-001<br>
     * UBL syntax path: cac:ContractingParty.Party.EndpointID<br>
     */
    @NotNull
    private String electronicAddressID;

    /**
     * Contracting body website
     * <p>
     * The website of the contracting body.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: ?<br>
     * BusReqID: ?<br>
     * UBL syntax path: cac:ContractingParty.Party.WebsiteURIID<br>
     */
    private String webSiteURI;

    /**
     * Postal address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 1..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: cac:ContractingParty.Party.PostalAddress.<br>
     */
    private PostalAddress postalAddress;

    /**
     * Contacting details
     * <p>
     * Used to provide contacting information for a party in general or a person.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: cac:ContractingParty.Party.Contact.<br>
     */
    private ContactingDetails contactingDetails;

    
    private String procurementPublicationURI;

    public String getProcurementPublicationURI() {
        return procurementPublicationURI;
    }

    public void setProcurementPublicationURI(String procurementPublicationURI) {
        this.procurementPublicationURI = procurementPublicationURI;
    }
    
    public String getCAOfficialName() {
        return caOfficialName;
    }

    public void setCAOfficialName(String caOfficialName) {
        this.caOfficialName = caOfficialName;
    }

    public String getProcurementProcedureTitle() {
        return procurementProcedureTitle;
    }

    public void setProcurementProcedureTitle(String procurementProcedureTitle) {
        this.procurementProcedureTitle = procurementProcedureTitle;
    }

    public String getProcurementProcedureDesc() {
        return procurementProcedureDesc;
    }

    public void setProcurementProcedureDesc(String procurementProcedureDesc) {
        this.procurementProcedureDesc = procurementProcedureDesc;
    }

    public String getProcurementProcedureFileReferenceNo() {
        return procurementProcedureFileReferenceNo;
    }

    public void setProcurementProcedureFileReferenceNo(String procurementProcedureFileReferenceNo) {
        this.procurementProcedureFileReferenceNo = procurementProcedureFileReferenceNo;
    }

    public String getCACountry() {
        return caCountry;
    }

    public void setCACountry(String caCountry) {
        this.caCountry = caCountry;
    }

    public String getProcurementPublicationNumber() {
        return procurementPublicationNumber;
    }

    public void setProcurementPublicationNumber(String procurementPublicationNumber) {
        this.procurementPublicationNumber = procurementPublicationNumber;
    }

    public String getElectronicAddressID() {
        return electronicAddressID;
    }

    public void setElectronicAddressID(String electronicAddressID) {
        this.electronicAddressID = electronicAddressID;
    }

    public String getWebSiteURI() {
        return webSiteURI;
    }

    public void setWebSiteURI(String webSiteURI) {
        this.webSiteURI = webSiteURI;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public ContactingDetails getContactingDetails() {
        return contactingDetails;
    }

    public void setContactingDetails(ContactingDetails contactingDetails) {
        this.contactingDetails = contactingDetails;
    }
}
