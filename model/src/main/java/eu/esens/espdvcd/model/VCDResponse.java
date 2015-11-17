package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface VCDResponse extends Serializable {

    /**
     * @return The id of the {@link VCDResponse}.
     */
    Long getId();

    /**
     * Available model types are in VCDResponseModelType.
     *
     * @return the model type
     */
    VCDResponseModelType getModelType();

    /**
     * @param type
     */
    void setModelType(VCDResponseModelType modelType);
}
