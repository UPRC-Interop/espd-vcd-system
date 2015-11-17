package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 * This interface is in charge to provide {@link EmbeddedDocument} data.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface EmbeddedDocument extends Serializable {

    /**
     * @return The id of the {@link EmbeddedDocument}.
     */
    Long getId();


    /**
     * @return The id of the parent owner ({@link VCDResponse}).
     */
    Long getParentId();

    /**
     * @param parentId
     */
    void setParentId(Long parentId);

    /**
     * @return The filename of the document
     * uniquely identifies document in the {@link VCDResponse}.
     */
    String getFileName();

    /**
     * @return The URI of the document
     * uniquely identifies document in the {@link VCDResponse}.
     */
    String getUri();

    /**
     * @return The file size.
     */
    Long getSize();


    /**
     * @return The type of the embedded document.
     */
    String getType();
}
