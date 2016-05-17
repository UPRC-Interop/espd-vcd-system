package eu.esens.espdvcd.model;

/**
 * Attachment
 *
 * Information about an attached document.
 *
 *
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class Attachment {

    /**
     * Attachment identifier
     * <p>
     * An identifier that can be used to reference the attached document, such as an unique identifier.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-154<br>
     * BusReqID: tbr92-017, tbr92-022<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.ID<br>
     */
    private String ID;


    /**
     * Attachment description
     * <p>
     * A short description of the attached document. Remark: despite cardinality 0..n implemented as String - FIXME
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..n<br>
     * InfReqID: tir92-155<br>
     * BusReqID: tbr92-017,tbr92-022 <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.DocumentDescription<br>
     */
    private String description;


    /**
     * Attachment description code
     * <p>
     * A functional description of the attachment expressed as code.
     * <p>
     * Data type: Code<br>
     * Cardinality:0..1 <br>
     * InfReqID:  tir92-156<br>
     * BusReqID: tbr92-017, tbr92-022<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.DocumentTypeCode<br>
     */
    private String descriptionCode;


    /**
     * Attached document
     * <p>
     * An attached document embedded as binary object. Remark: implemented as String - FIXME
     * <p>
     * Data type: Binary Object<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-157<br>
     * BusReqID: tbr92-017, tbr92-022<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response. Evidence.EvidenceDocumentReference.Attachment.EmbeddedDocumentBinaryObject<br>
     */
    private String document;


    public Attachment(String ID, String description, String descriptionCode, String document) {
        this.ID = ID;
        this.description = description;
        this.descriptionCode = descriptionCode;
        this.document = document;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }

    public void setDescriptionCode(String descriptionCode) {
        this.descriptionCode = descriptionCode;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
