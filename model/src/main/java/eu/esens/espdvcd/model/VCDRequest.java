package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.VCDRequestModelType;

import java.io.Serializable;

/**
 * TODO: Add description.
 *
 */
public interface VCDRequest extends Serializable {
    /**
     * @return The ID of the {@link VCDRequest}.
     */
    Long getId();

    /**
     * Available model types are in VCDRequestModelType.
     *
     * @return the model type
     */
    VCDRequestModelType getModelType();

    /**
     * @param modelType
     */
    void setModelType(VCDRequestModelType modelType);

}
