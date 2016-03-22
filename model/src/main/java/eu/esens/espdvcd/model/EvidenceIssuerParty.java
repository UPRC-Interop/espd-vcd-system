package eu.esens.espdvcd.model;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceIssuerParty {

    /**
     * Evidence issuer party identifier
     * <p>
     * The identifier of the party issuer of the evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-143<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PartyIdentification<br>
     */
    private String ID;


    /**
     * Evidence issuer party name
     * <p>
     * The name of the party issuer of the evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-144<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PartyName<br>
     */
    private String name;


    /**
     * Postal Address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty.PostalAddress<br>
     */
    private PostalAddress postalAddress;

    public EvidenceIssuerParty(String ID, String name, PostalAddress postalAddress) {
        this.ID = ID;
        this.name = name;
        this.postalAddress = postalAddress;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }
}
