package eu.esens.espdvcd.designer.toop.iface.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ToopRequest {
    private String documentUUID;
    private LocalDate issueDate;
    private LocalTime issueTime;
    private boolean copyIndicator;
    private String documentTypeID;
    private String specificationID;
    private String dcDocumentID;
    private DataConsumer dataConsumer;
    private DataElementRequest dataElementRequest;

    public String getDocumentUUID() {
        return documentUUID;
    }

    public void setDocumentUUID(String documentUUID) {
        this.documentUUID = documentUUID;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalTime issueTime) {
        this.issueTime = issueTime;
    }

    public boolean isCopyIndicator() {
        return copyIndicator;
    }

    public void setCopyIndicator(boolean copyIndicator) {
        this.copyIndicator = copyIndicator;
    }

    public String getDocumentTypeID() {
        return documentTypeID;
    }

    public void setDocumentTypeID(String documentTypeID) {
        this.documentTypeID = documentTypeID;
    }

    public String getSpecificationID() {
        return specificationID;
    }

    public void setSpecificationID(String specificationID) {
        this.specificationID = specificationID;
    }

    public String getDcDocumentID() {
        return dcDocumentID;
    }

    public void setDcDocumentID(String dcDocumentID) {
        this.dcDocumentID = dcDocumentID;
    }

    public DataConsumer getDataConsumer() {
        return dataConsumer;
    }

    public void setDataConsumer(DataConsumer dataConsumer) {
        this.dataConsumer = dataConsumer;
    }
}
