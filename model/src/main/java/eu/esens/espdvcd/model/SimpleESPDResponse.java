package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.persisted.PersistedESPDRequest;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.net.URL;
import java.util.List;

/**
 * TODO: Add description.
 *
 */
public class SimpleESPDResponse extends PersistedESPDRequest implements ESPDResponse {

    private static final long serialVersionUID = -3343982328572347289L;
    
    
    @Override
    public void setEspdResponseType(ESPDResponseType espdResponseType) {

    }

    @Override
    public List<URL> getExternalDocuments() {
        return null;
    }

    @Override
    public ESPDResponseType getEspdResponseType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectableCriterion> getSelectionCriteriaList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectableCriterion> getExclusionCriteriaList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectableCriterion> getEORelatedCriteriaList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
