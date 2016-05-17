package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.VCDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;

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

    /**
     * @return The {@link ESPDRequestType} of the {@link VCDRequest}.
     */
    ESPDRequestType getVcdRequestType();

    /**
     * @param vcdRequestType
     */
    void setVcdRequestType(ESPDRequestType vcdRequestType);
}
