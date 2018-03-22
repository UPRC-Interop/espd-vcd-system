package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.VCDResponseModelType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.util.List;

/**
 * Not used.
 *
 */
public class VCDResponseImpl implements VCDResponse {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public VCDResponseModelType getModelType() {
        return null;
    }

    @Override
    public void setModelType(VCDResponseModelType modelType) {

    }

    @Override
    public ESPDResponseType getVcdResponseType() {
        return null;
    }

    @Override
    public void setVcdResponseType(ESPDResponseType vcdResponseType) {

    }

    @Override
    public List<EmbeddedDocument> getEmbeddedDocuments() {
        return null;
    }
}
