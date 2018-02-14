package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.VCDResponseModelType;

import java.io.Serializable;
import java.util.List;

/**
 * Not used.
 *
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
     * @return list of {@link EmbeddedDocument} of the {@link VCDResponse}.
     */
    List<EmbeddedDocument> getEmbeddedDocuments();
}
