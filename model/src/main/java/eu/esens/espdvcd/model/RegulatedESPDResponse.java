package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO implementation of {@link ESPDResponse}.
 *
 */
public class RegulatedESPDResponse extends RegulatedESPDRequest implements ESPDResponse {

    private static final long serialVersionUID = -3343982328572347289L;
    
    protected EODetails eoDetails;
    private ESPDRequestDetails espdRequestDetails;
    private List<Evidence> evidenceList;

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

    @Override
    public void setEvidenceList(List<Evidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    @Override
    public List<Evidence> getEvidenceList() {
        if (evidenceList == null) {
            evidenceList = new ArrayList<>();
        }
        return evidenceList;
    }

}
