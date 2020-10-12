/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Reference to an ESPD Request document.
 *
 * Created by Ulf Lotzmann.
 */
public class ESPDRequestDetails {

    /**
     * ESPD request identifier
     * <p>
     * The identifier of the ESPD Request.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-308<br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.ID<br>
     * <p>
     * This element refers to the following element in the ESPD Request:<br>
     * Document identifier
     * <p>
     * Identifier of a document An transaction instance must contain an identifier.
     * The identifier enables positive referencing the document instance for various
     * purposes including referencing between transactions that are part of the same process.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-001<br>
     * BusReqID: tbr70-002<br>
     * UBL syntax path: cbc:ID<br>
     */
    private String id;

    /**
     * ESPD request issue date
     * <p>
     * The issue date of the ESPD Request.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-310<br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.IssueDate<br>
     * <p>
     * This element refers to the following element in the ESPD Request:<br>
     * Document issue date
     * <p>
     * Date when the referred document was issued.
     * <p>
     * Data type: Date<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-002<br>
     * BusReqID: tbr70-002<br>
     * UBL syntax path: cbc:IssueDate<br>
     */
    private LocalDate issueDate;

    /**
     * ESPD request issue time
     * <p>
     * The issue time of the ESPD Request.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-311<br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.IssueTime<br>
     * <p>
     * This element refers to the following element in the ESPD Request:<br>
     * Document issue time
     * <p>
     * Time when the document was issued.
     * <p>
     * Data type: Time<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-003<br>
     * BusReqID: tbr70-002<br>
     * UBL syntax path: cbc:IssueTime<br>
     */
    private LocalTime issueTime;

    /**
     * ESPD request description
     * <p>
     * Short description of the ESPD Request.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-313<br>
     * BusReqID: tbr92-013<br>
     * UBL syntax path: cac:AdditionalDocumentReference.DocumentDescription<br>
     * <p>
     * This element refers to the following element in the ESPD Request:<br>
     * Reference number
     * <p>
     * An identifier that is specified by the buyer and used as a reference number for all documents
     * in the procurement process. It is also known as procurement project identifier, procurement
     * reference number or contract folder identifier. A reference to the procurement process to
     * which this Qualification document is delivered as a response.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-005<br>
     * BusReqID: tbr70-007<br>
     * UBL syntax path: cbc:ContractFolderID<br>
     */
    private String description;

    public ESPDRequestDetails() {
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
