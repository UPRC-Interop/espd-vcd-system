package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.EmbeddedDocumentModelType;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public class EmbeddedDocumentImpl implements EmbeddedDocument {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Long getParentId() {
        return null;
    }

    @Override
    public void setParentId(Long parentId) {

    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public String getUri() {
        return null;
    }

    @Override
    public Long getSize() {
        return null;
    }

    @Override
    public EmbeddedDocumentModelType getModelType() {
        return null;
    }
}
