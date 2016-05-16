package eu.esens.espdvcd.model;

import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * This interface is in charge to provide {@link ESPDResponse} data.
 *
 */
public interface ESPDResponse extends ESPDRequest,Serializable {

    public EODetails getEODetails();
    
    public void setEODetails(EODetails eoDetails);
    /**
     * @return The {@link ESPDResponseType} of the {@link ESPDResponse}.
     */
    public ESPDResponseType getEspdResponseType();

    /**
     * @param espdResponseType
     */
    void setEspdResponseType(ESPDResponseType espdResponseType);

    /**
     * @return list of {@link URL} of the {@link ESPDResponse}.
     */
    List<URL> getExternalDocuments();

}
