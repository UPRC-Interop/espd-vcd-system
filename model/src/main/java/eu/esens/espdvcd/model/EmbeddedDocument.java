package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.EmbeddedDocumentModelType;

import java.io.Serializable;

/**
 * This interface is in charge to provide {@link EmbeddedDocument} data.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface EmbeddedDocument extends Serializable {

    /**
     * @return The ID of the {@link EmbeddedDocument}.
     */
    Long getId();


    /**
     * @return The id of the parent owner ({@link ESPDResponse} or {@link VCDResponse}).
     */
    Long getParentId();

    /**
     * @param parentId
     */
    void setParentId(Long parentId);

    /**
     * @return The filename of the document
     * uniquely identifies document in the {@link ESPDResponse} or {@link VCDResponse}.
     */
    String getFileName();

    /**
     * @return The URI of the document
     * uniquely identifies document in the {@link ESPDResponse} or {@link VCDResponse}.
     */
    String getUri();

    /**
     * @return The file size of the document.
     */
    Long getSize();


    /**
     * @return The model type of the embedded document.
     */
    EmbeddedDocumentModelType getModelType();
}
