package eu.esens.espdvcd.model;

import javax.validation.constraints.NotNull;

/**
 * The party providing the service.
 *
 * Created by Ulf Lotzmann on 28/03/2017.
 */
public class ServiceProviderDetails {

    /**
     * Service provider name
     * <p>
     * The name of the service provider. Issuer body of the ESPD.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-040, tir92-021<br>
     * BusReqID: tbr70-011, tbr92-021<br>
     * UBL syntax path: cac:ServiceProviderParty.Party.PartyName.Name<br>
     */
    private String name;

    /**
     * Service provider identifier
     * <p>
     * The national identifier of a service provider as it is legally registered (e.g. VAT identification, such as KBO) Issuer body of the ESPD.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-041, tir92-022<br>
     * BusReqID: tbr70-011, tbr92-021<br>
     * UBL syntax path: cac:ServiceProviderParty.Party.PartyIdentification<br>
     */
    private String id;

    /**
     * Service provider electronic address identifier
     * <p>
     * Electronic address of the service provider. Issuer body of the ESPD.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-042, tir92-023<br>
     * BusReqID: tbr70-011, tbr92-021<br>
     * UBL syntax path: cac:ServiceProviderParty.Party.EndpointID<br>
     */
    private String endpointID;

    /**
     * Service provider website
     * <p>
     * The website of the service provider.
     * <p>
     * Data type: Identifier <br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-315, tir92-315<br>
     * BusReqID: tbr70-011, tbr92-021<br>
     * UBL syntax path: cac: ServiceProviderParty.Party.WebsiteURIID<br>
     */
    private String websiteURI;

    /**
     * Postal address
     * <p>
     * Address information.
     * <p>
     * Data type: <br>
     * Cardinality: 1..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: cac:ServiceProviderParty.Party.PostalAddress.<br>
     */
    private PostalAddress postalAddress;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getEndpointID() {
        return endpointID;
    }

    public void setEndpointID(String endpointID) {
        this.endpointID = endpointID;
    }

    public String getWebsiteURI() {
        return websiteURI;
    }

    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }
}
