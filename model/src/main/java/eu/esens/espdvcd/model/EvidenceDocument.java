package eu.esens.espdvcd.model;

/**
 * FIXME: not used
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class EvidenceDocument {

    /**
     * External document URI
     * <p>
     * The Uniform Resource Identifier (URI) that identifies where the external document is located.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-151<br>
     * BusReqID: tbr92-017, tbr92-022<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.Attachment.ExternalReference.URI<br>
     */
    private String externalDocumentURI;


    /**
     * Document digest
     * <p>
     * Digest of the document.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-152<br>
     * BusReqID: tbr92-017,  tbr92-022<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.Attachment.ExternalReference.DocumentHash<br>
     */
    private String documentDigest;


    /**
     * Document digest method code
     * <p>
     * Code that indicates the algorithm used to calculate the hash.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-153<br>
     * BusReqID: tbr92-017, tbr92-022<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.Attachment.ExternalReference.HashAlgorithmMethod<br>
     */
    private String documentDigestMethodCode;


    /**
     * Attachment
     * <p>
     * Information about an attached document.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference<br>
     */
    private Attachment attachment;

    public EvidenceDocument(String externalDocumentURI, String documentDigest, String documentDigestMethodCode, Attachment attachment) {
        this.externalDocumentURI = externalDocumentURI;
        this.documentDigest = documentDigest;
        this.documentDigestMethodCode = documentDigestMethodCode;
        this.attachment = attachment;
    }

    public String getExternalDocumentURI() {
        return externalDocumentURI;
    }

    public void setExternalDocumentURI(String externalDocumentURI) {
        this.externalDocumentURI = externalDocumentURI;
    }

    public String getDocumentDigest() {
        return documentDigest;
    }

    public void setDocumentDigest(String documentDigest) {
        this.documentDigest = documentDigest;
    }

    public String getDocumentDigestMethodCode() {
        return documentDigestMethodCode;
    }

    public void setDocumentDigestMethodCode(String documentDigestMethodCode) {
        this.documentDigestMethodCode = documentDigestMethodCode;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

}
