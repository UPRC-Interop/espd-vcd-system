package eu.esens.espdvcd.model;

import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.ArtefactType;
import eu.esens.espdvcd.schema.EDMVersion;


import java.io.Serializable;

public class DocumentDetails implements Serializable {

    private static final long serialVersionUID = 5723048808969930122L;
    /**
     * Class that contains information about the ESPD document
     */

    private final EDMVersion version;
    private final ArtefactType artefactType;
    private final QualificationApplicationTypeEnum qualificationApplicationType;

    /**
     * Constructor for the DocumentDetails object
     *
     * @param version                      version of the ESPD (EDMVersion enum: V1, V2)
     * @param type                         type of the ESPD (ArtefactType enum: ESPD_REQUEST, ESPD_RESPONSE)
     * @param qualificationApplicationType whether the ESPD is SELF_CONTAINED or REQULATED
     */
    public DocumentDetails(EDMVersion version, ArtefactType type, QualificationApplicationTypeEnum qualificationApplicationType) {
        this.version = version;
        this.artefactType = type;
        this.qualificationApplicationType = qualificationApplicationType;
    }

    public EDMVersion getVersion() {
        return version;
    }

    public ArtefactType getType() {
        return artefactType;
    }

    public QualificationApplicationTypeEnum getQualificationApplicationType() {
        return qualificationApplicationType;
    }
}
