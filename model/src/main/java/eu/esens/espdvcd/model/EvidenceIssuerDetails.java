package eu.esens.espdvcd.model;

/**
 * Evidence issuer party
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceIssuerDetails {

    /**
     * Evidence issuer party identifier
     * <p>
     * The identifier of the party issuer of the evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-143<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails.PartyIdentification<br>
     */
    private String ID;


    /**
     * Evidence issuer party name
     * <p>
     * The name of the party issuer of the evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-144<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails.PartyName<br>
     */
    private String name;


    /**
     * Evidence issuer website
     * <p>
     * The website of the party issuer of the evidence.
     * <p>
     * Data type: WebsiteURIID<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-322<br>
     * BusReqID: <br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails.WebsiteURIID<br>
     */
    private String website;

    public EvidenceIssuerDetails() {
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

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

}
