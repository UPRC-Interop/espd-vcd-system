package eu.esens.espdvcd.model;

import java.net.URL;
import java.util.List;

/**
 * TODO: Add description.
 *
 */
public class SimpleESPDResponse extends SimpleESPDRequest implements ESPDResponse {

    private static final long serialVersionUID = -3343982328572347289L;
    
    protected EODetails eoDetails;

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

}
