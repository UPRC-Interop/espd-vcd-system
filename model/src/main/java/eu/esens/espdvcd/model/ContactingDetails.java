package eu.esens.espdvcd.model;

/**
 * Contacting details
 *
 * Used to provide contacting information for a party in general or a person.
 *
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class ContactingDetails {


    /**
     * Contact point
     * <p>
     * The name of the contact point.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-047<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty. Party. Contact.Name<br>
     */
    private String contactPointName;


    /**
     * Contact fax number
     * <p>
     * A fax number for the contact point.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-048<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party. Contact.Telephone<br>
     */
    private String faxNumber;


    /**
     * Contact telephone number
     * <p>
     * A phone number for the contact point.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-049<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty.Party. Contact.Telefax<br>
     */
    private String telephoneNumber;


    /**
     * Contact email address
     * <p>
     * An e-mail address for the contact point.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-050<br>
     * BusReqID: tbr92-002<br>
     * UBL syntax path: espd-cac:EconomicOperatorParty. Party. Contact.ElectronicMail<br>
     */
    private String emailAddress;


    public String getContactPointName() {
        return contactPointName;
    }

    public void setContactPointName(String contactPointName) {
        this.contactPointName = contactPointName;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
