package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.VCDResponseModelType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.io.Serializable;
import java.util.List;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface VCDResponse extends Serializable {
    /**
     * @return The ID of the {@link VCDResponse}.
     */
    Long getId();

    /**
     * Available model types are in VCDResponseModelType.
     *
     * @return the model type
     */
    VCDResponseModelType getModelType();

    /**
     * @param modelType
     */
    void setModelType(VCDResponseModelType modelType);

    /**
     * @return The {@link ESPDResponseType} of the {@link VCDResponse}.
     */
    ESPDResponseType getVcdResponseType();

    /**
     * @param vcdResponseType
     */
    void setVcdResponseType(ESPDResponseType vcdResponseType);

    /**
     * @return list of {@link EmbeddedDocument} of the {@link VCDResponse}.
     */
    List<EmbeddedDocument> getEmbeddedDocuments();
}
