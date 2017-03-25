package eu.esens.espdvcd.model;

import java.util.List;
import java.util.ArrayList;

/**
 * FIXME: not used
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class Evidence {

    /**
     * Evidence identifier
     * <p>
     * Identifier for an evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-137<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.ID<br>
     */
    private String ID;


    /**
     * Evidence type code
     * <p>
     * Type code for an evidence.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-138<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.DocumentTypeCode<br>
     */
    private String typeCode;


    /**
     * Evidence name
     * <p>
     * The name of an evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-139<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceName<br>
     */
    private String name;


    /**
     * Embedded evidence indicator
     * <p>
     * Indicates whether the ESPD must contain the evidence embedded (true) or it is just necessary
     * to identify the evidence using a reference such as a URL (false).
     * <p>
     * Data type: Indicator<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-140<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EmbeddedEvidenceIndicator<br>
     */
    private boolean embeddedEvidenceIndicator;


    /**
     * Evidence description
     * <p>
     * A textual description of the evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-141<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.DocumentDescription<br>
     */
    private String description;


    /**
     * Evidence version identifier
     * <p>
     * Version identifier for an evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-142<br>
     * BusReqID: tbr92-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.VersionID<br>
     */
    private String versionID;


    /**
     * Evidence issuer party
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerParty<br>
     */
    private EvidenceIssuerParty issuerParty;


    /**
     * Evidence document
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.Attachment.ExternalReference<br>
     */
    private List<EvidenceDocument> documents;


    public Evidence(String ID, String typeCode, String name, boolean embeddedEvidenceIndicator, String description, String versionID, EvidenceIssuerParty issuerParty) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.embeddedEvidenceIndicator = embeddedEvidenceIndicator;
        this.description = description;
        this.versionID = versionID;
        this.issuerParty = issuerParty;
    }

    public Evidence(String ID, String typeCode, String name, boolean embeddedEvidenceIndicator, String description, String versionID, EvidenceIssuerParty issuerParty, List<EvidenceDocument> documents) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.embeddedEvidenceIndicator = embeddedEvidenceIndicator;
        this.description = description;
        this.versionID = versionID;
        this.issuerParty = issuerParty;
        this.documents = documents;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmbeddedEvidenceIndicator() {
        return embeddedEvidenceIndicator;
    }

    public void setEmbeddedEvidenceIndicator(boolean embeddedEvidenceIndicator) {
        this.embeddedEvidenceIndicator = embeddedEvidenceIndicator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public EvidenceIssuerParty getIssuerParty() {
        return issuerParty;
    }

    public void setIssuerParty(EvidenceIssuerParty issuerParty) {
        this.issuerParty = issuerParty;
    }

    public List<EvidenceDocument> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<>();
        }
        return documents;
    }

    public void setDocuments(List<EvidenceDocument> documents) {
        this.documents = documents;
    }
}
