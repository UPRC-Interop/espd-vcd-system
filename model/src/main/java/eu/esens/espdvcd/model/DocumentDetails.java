package eu.esens.espdvcd.model;

import java.io.Serializable;

public class DocumentDetails implements Serializable {

    private static final long serialVersionUID = 5723048808969930122L;
    /**
     * Class that contains information about the ESPD document
     */

    private final String version;
    private final String artefactType;
    private final String qualificationApplicationType;

    /**
     * Constructor for the DocumentDetails object
     *
     * @param version                      version of the ESPD (EDMVersion enum: V1, V2)
     * @param type                         type of the ESPD (ArtefactType enum: ESPD_REQUEST, ESPD_RESPONSE)
     * @param qualificationApplicationType whether the ESPD is SELF_CONTAINED or REQULATED
     */
    public DocumentDetails(String version, String type, String qualificationApplicationType) {
        this.version = version;
        this.artefactType = type;
        this.qualificationApplicationType = qualificationApplicationType;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return artefactType;
    }

    public String getQualificationApplicationType() {
        return qualificationApplicationType;
    }
}
