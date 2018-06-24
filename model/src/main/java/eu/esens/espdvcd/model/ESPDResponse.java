package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;

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
    EODetails getEODetails();
    
    /**
     * 
     * @param eoDetails the {@link EODetails} to be assigned to the ESPD Response
     * Object.
     */
    void setEODetails(EODetails eoDetails);

    /**
     * @return list of {@link URL} of the {@link ESPDResponse}.
     */
    List<URL> getExternalDocuments();

    /**
     * @param espdRequestDetails the (@link ESPDReqeustDetails) linked to this ESPD Response
     */
    void setESPDRequestDetails(ESPDRequestDetails espdRequestDetails);

    /**
     * @return the (@link ESPDRequestDetails) linked to this ESPD Response
     */
    ESPDRequestDetails getESPDRequestDetails();

    void setEvidenceList(List<Evidence> evidenceList);

    List<Evidence> getEvidenceList();

    void setResponseList(List<Response> responseList);

    List<Response> getResponseList();

}
