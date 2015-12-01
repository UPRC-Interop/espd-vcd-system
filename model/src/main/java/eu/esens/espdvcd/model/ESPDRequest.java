package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDRequestModelType;

import java.io.Serializable;

/**
 * This interface is in charge to provide {@link ESPDRequest} data.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface ESPDRequest extends Serializable {
    /**
     * @return The ID of the {@link ESPDRequest}.
     */
    Long getId();

    /**
     * Available model types are in ESPDRequestModelType.
     *
     * @return the model type
     */
    ESPDRequestModelType getModelType();

    /**
     * @param modelType
     */
    void setModelType(ESPDRequestModelType modelType);

    /**
     * @return The {@link grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType} of the {@link ESPDRequest}.
     */
    grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType getEspdRequestType();

    /**
     * @param espdRequestType
     */
    void setEspdRequestType(grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType espdRequestType);
}
