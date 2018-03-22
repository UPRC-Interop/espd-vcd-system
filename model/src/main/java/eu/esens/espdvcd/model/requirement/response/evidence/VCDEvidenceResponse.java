package eu.esens.espdvcd.model.requirement.response.evidence;

import eu.esens.espdvcd.model.EvidenceIssuerDetails;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The evidence response with URI to container-local evidence document and document meta-information.<br>
 *
 *
 *
 * Created by Ulf Lotzmann on 26/04/2017.
 */
public class VCDEvidenceResponse extends EvidenceURLResponse {


    /*
     * Evidence identifier --> provided by Response class
     * <p>
     * Identifier for an evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-137<br>
     * BusReqID: tbr041-017<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.ID<br>
     */


    /*
    * External document URI --> provided by EvidenceURLResponse class
    * <p>
    * It contains the Uniform Resource Identifier (URI) that identifies where the document within the
    * ASiC Container is located. Reference to data objects within the container shall be relative URIs
    * following the ASiC Container specifications.<br>
    * Remark: deviation from the VCDResponse specification: only one document can be handled with this response
    * <p>
    * InfReqID: tir041-151<br>
    * BusReqID: tbr041-017, tbr041-022, tbr041-006, tbr041-007<br>
    * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.
    *      Evidence.EvidenceDocumentReference.Attachment.ExternalReference.URI<br>
    */



    /**
     * Evidence issue date
     * <p>
     * The date, assigned by the sender of the referenced document, on which the document was issued.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-321<br>
     * BusReqID: tbr041-023<br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.IssueDate<br>
     */
    private LocalDate date;

    /**
     * Evidence issue time
     * <p>
     * The time, assigned by the sender of the referenced document, on which the document was issued.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-322<br>
     * BusReqID: tbr041-023<br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.IssueTime<br>
     */
    private LocalTime time;


    /**
     * Evidence type code
     * <p>
     * Type code for an evidence.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-138<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path:
     *      ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.DocumentTypeCode<br>
     */
    private String typeCode;


    /**
     * Evidence name
     * <p>
     * The name of an evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-1399<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceName<br>
     */
    private String name;


    /**
     * Embedded evidence indicator
     * <p>
     * Indicates whether the ESPD must contain the evidence embedded (true) or it is just necessary
     * to identify the evidence using a reference such as a URL (false).<br>
     * The element is used to check if all evidences are included.
     * <p>
     * Data type: Indicator<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir041-140<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
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
     * InfReqID: tir041-141<br>
     * BusReqID: tbr041-017, tbr041-007, tbr041-006<br>
     * UBL syntax path:
     *   ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.DocumentDescription<br>
     */
    private String description;



    /**
     * Evidence issuer party
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceIssuerDetails<br>
     */
    private EvidenceIssuerDetails evidenceIssuer;


    public VCDEvidenceResponse() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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

    public EvidenceIssuerDetails getEvidenceIssuer() {
        return evidenceIssuer;
    }

    public void setEvidenceIssuer(EvidenceIssuerDetails evidenceIssuer) {
        this.evidenceIssuer = evidenceIssuer;
    }
}

