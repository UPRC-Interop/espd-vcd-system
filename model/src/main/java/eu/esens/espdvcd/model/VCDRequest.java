package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface VCDRequest extends Serializable {

    /**
     * @return The id of the {@link VCDRequest}.
     */
    Long getId();

    /**
     * Available model types are in VCDRequestModelType.
     *
     * @return the model type
     */
    VCDRequestModelType getModelType();

    /**
     * @param type
     */
    void setModelType(VCDRequestModelType modelType);
}
