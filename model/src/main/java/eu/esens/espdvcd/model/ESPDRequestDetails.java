package eu.esens.espdvcd.model;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Reference to an ESPD Request document.
 *
 */
public class ESPDRequestDetails {

    /**
     * Document identifier
     * <p>
     * Identifier of a document An transaction instance must contain an identifier.
     * The identifier enables positive referencing the document instance for various
     * purposes including referencing between transactions that are part of the same process.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-001<br>
     * BusReqID: tbr92-019<br>
     * UBL syntax path: cbc:ID<br>
     */
    private String id;

    /**
     * Document issue date
     * <p>
     * Date when the referred document was issued.
     * <p>
     * Data type: Date<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-002<br>
     * BusReqID: tbr92-019<br>
     * UBL syntax path: cbc:IssueDate<br>
     */
    private XMLGregorianCalendar issueDate;

    /**
     * Document issue time
     * <p>
     * Time when the document was issued.
     * <p>
     * Data type: Time<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-003<br>
     * BusReqID: tbr92-019<br>
     * UBL syntax path: cbc:IssueTime<br>
     */
    private XMLGregorianCalendar issueTime;

    /**
     * Reference number
     * <p>
     * An identifier that is specified by the buyer and used as a reference number for all documents
     * in the procurement process. It is also known as procurement project identifier, procurement
     * reference number or contract folder identifier. A reference to the procurement process to
     * which this Qualification document is delivered as a response.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-013<br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cbc:ContractFolderID<br>
     */
    private String referenceNumber;

    public ESPDRequestDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public XMLGregorianCalendar getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(XMLGregorianCalendar issueDate) {
        this.issueDate = issueDate;
    }

    public XMLGregorianCalendar getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(XMLGregorianCalendar issueTime) {
        this.issueTime = issueTime;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
