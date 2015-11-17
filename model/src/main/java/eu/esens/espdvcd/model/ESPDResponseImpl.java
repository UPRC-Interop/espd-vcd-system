package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDResponseModelType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.net.URL;
import java.util.List;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public class ESPDResponseImpl implements ESPDResponse {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public ESPDResponseModelType getModelType() {
        return null;
    }

    @Override
    public void setModelType(ESPDResponseModelType modelType) {

    }

    @Override
    public ESPDResponseType getEspdResponseType() {
        return null;
    }

    @Override
    public void setEspdResponseType(ESPDResponseType espdResponseType) {

    }

    @Override
    public List<URL> getExternalDocuments() {
        return null;
    }

    @Override
    public List<EmbeddedDocument> getEmbeddedDocuments() {
        return null;
    }
}
