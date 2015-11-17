package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDResponseModelType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * This interface is in charge to provide {@link ESPDResponse} data.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface ESPDResponse extends Serializable {
    /**
     * @return The ID of the {@link ESPDResponse}.
     */
    Long getId();

    /**
     * Available model types are in ESPDResponseModelType.
     *
     * @return the model type
     */
    ESPDResponseModelType getModelType();

    /**
     * @param modelType
     */
    void setModelType(ESPDResponseModelType modelType);

    /**
     * @return The {@link ESPDResponseType} of the {@link ESPDResponse}.
     */
    ESPDResponseType getEspdResponseType();

    /**
     * @param espdResponseType
     */
    void setEspdResponseType(ESPDResponseType espdResponseType);

    /**
     * @return list of {@link URL} of the {@link ESPDResponse}.
     */
    List<URL> getExternalDocuments();


    /**
     * @return list of {@link EmbeddedDocument} of the {@link ESPDResponse}.
     */
    List<EmbeddedDocument> getEmbeddedDocuments();
}
