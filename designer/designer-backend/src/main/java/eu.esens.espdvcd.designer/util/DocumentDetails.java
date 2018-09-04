package eu.esens.espdvcd.designer.util;

import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.schema.EDMVersion;

public class DocumentDetails {

    private final EDMVersion version;
    private final ArtefactType artefactType;

    public DocumentDetails(EDMVersion version, ArtefactType type) {
        this.version = version;
        this.artefactType = type;
    }

    public EDMVersion getVersion() {
        return version;
    }

    public ArtefactType getType() {
        return artefactType;
    }
}
