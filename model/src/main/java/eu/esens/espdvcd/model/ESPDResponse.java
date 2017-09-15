package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * This interface is in charge to provide {@link ESPDResponse} data.
 *
 */
public interface ESPDResponse extends ESPDRequest, Serializable {

    /**
     *
     * @return the EO Details assigned to the ESPD Response Object
     */
    public EODetails getEODetails();

    /**
     *
     * @param eoDetails the {@link EODetails} to be assigned to the ESPD Response Object.
     */
    public void setEODetails(EODetails eoDetails);

    /**
     * @return list of {@link URL} of the {@link ESPDResponse}.
     */
    List<URL> getExternalDocuments();

}
