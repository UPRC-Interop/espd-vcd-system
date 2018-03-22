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

    /**
     * 
     * @return the EO Details assigned to the ESPD Response Object 
     */
    EODetails getEODetails();
    
    /**
     * 
     * @param eoDetails the {@link EODetails} to be assigned to the ESPD Response
     * Object.
     */
    void setEODetails(EODetails eoDetails);
    /**
     * @return The {@link ESPDResponseType} of the {@link ESPDResponse}.
     */
    ESPDResponseType getEspdResponseType();

    /**
     * @param espdResponseType
     */
    void setEspdResponseType(ESPDResponseType espdResponseType);

    /**
     * @return list of {@link URL} of the {@link ESPDResponse}.
     */
    List<URL> getExternalDocuments();

    /**
     * @param espdRequestDetails the (@link ESPDReqeustDetails) linked to this ESPD Response
     */
    void setESPDRequestDetails(ESPDRequestDetails espdRequestDetails);

    /**
     * @return the (@link ESPDReqeustDetails) linked to this ESPD Response
     */
    ESPDRequestDetails getESPDRequestDetails();

}
