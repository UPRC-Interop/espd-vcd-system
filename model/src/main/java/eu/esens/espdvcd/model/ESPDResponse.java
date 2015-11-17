package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 * This interface is in charge to provide {@link ESPDResponse} data.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface ESPDResponse extends Serializable {

    /**
     * @return The id of the {@link ESPDResponse}.
     */
    Long getId();

    /**
     * Available model types are in ESPDResponseModelType.
     *
     * @return the model type
     */
    ESPDResponseModelType getModelType();

    /**
     * @param type
     */
    void setModelType(ESPDResponseModelType modelType);
}
