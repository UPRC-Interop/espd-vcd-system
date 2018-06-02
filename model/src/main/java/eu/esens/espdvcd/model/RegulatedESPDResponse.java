package eu.esens.espdvcd.model;

import java.net.URL;
import java.util.List;

/**
 * POJO implementation of {@link ESPDResponse}.
 *
 */
public class RegulatedESPDResponse extends RegulatedESPDRequest implements ESPDResponse {

    private static final long serialVersionUID = -3343982328572347289L;
    
    protected EODetails eoDetails;
    private ESPDRequestDetails espdRequestDetails;

    public EODetails getEODetails() {
        return eoDetails;
    }

    public void setEODetails(EODetails eoDetails) {
        this.eoDetails = eoDetails;
    }
    
    
    @Override
    public List<URL> getExternalDocuments() {
        return null;
    }

    @Override
    public void setESPDRequestDetails(ESPDRequestDetails espdRequestDetails) {
        this.espdRequestDetails = espdRequestDetails;
    }

    @Override
    public ESPDRequestDetails getESPDRequestDetails() {
        return this.espdRequestDetails;
    }

}
