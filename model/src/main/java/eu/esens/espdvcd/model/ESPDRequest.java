package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 * This interface is in charge to provide {@link ESPDRequest} data.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface ESPDRequest extends Serializable {

    /**
     * @return The id of the {@link ESPDRequest}.
     */
    Long getId();

    /**
     * Available model types are in ESPDRequestModelType.
     *
     * @return the model type
     */
    ESPDRequestModelType getModelType();

    /**
     * @param type
     */
    void setModelType(ESPDRequestModelType modelType);
}
