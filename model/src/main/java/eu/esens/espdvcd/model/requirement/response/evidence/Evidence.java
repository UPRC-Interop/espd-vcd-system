/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.requirement.response.evidence;

import eu.esens.espdvcd.model.EvidenceIssuerDetails;

import java.io.Serializable;

/**
 * The evidence model class for EDM v2.0.2
 *
 */
public class Evidence implements Serializable {

    private static final long serialVersionUID = -1518098779888363639L;

    /**
     * Evidence identifier
     * <p>
     * Identifier of the evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir92-137<br>
     * BusReqID: tbr92-017tbr9<br>
     * UBL syntax path: cac:Evidence/cbc:ID<br>
     */
    private String ID;

    /**
     * Evidence name
     * <p>
     * Name of the evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-139<br>
     * BusReqID: tbr92-017tbr9<br>
     * UBL syntax path: cac:Evidence/cbc:Name<br>
     */
    private String Name;

    /**
     * Evidence type code
     * <p>
     * Name of the evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-138<br>
     * BusReqID: tbr92-017tbr9<br>
     * UBL syntax path: cac:Evidence/cbc:EvidenceTypeCode<br>
     */
    private String typeCode;

    /**
     * Evidence description
     * <p>
     * The textual description for this Evidence.
     * Rule: Use this field to keep the Reference/Code of the Evidence.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-141<br>
     * BusReqID: tbr92-017tbr9<br>
     * UBL syntax path: cac:Evidence/cbc:Description<br>
     */
    private String description;

    /**
     * Response confidentiality level code
     * <p>
     * A code specifying the confidentiality level of this evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-142<br>
     * BusReqID: tbr92-017tbr9<br>
     * UBL syntax path: cac:Evidence/cbc:ConfidentialityLevelCode<br>
     */
    private String confidentialityLevelCode;

    /**
     * External document URI
     * <p>
     * A code specifying the confidentiality level of this evidence.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-151<br>
     * BusReqID: tbr92-017tbr9<br>
     * UBL syntax path: cac:Evidence/cac:DocumentReference/Attachment/ExternalReference/cbc:URI<br>
     */
    private String evidenceURL;

    /**
     * Evidence issuer party
     * <p>
     *
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: cac:Evidence/cac:DocumentReference/cac:IssuerParty<br>
     */
    private EvidenceIssuerDetails evidenceIssuer;

    public Evidence() {

    }

    public Evidence(String ID, String description, String confidentialityLevelCode, String evidenceURL, EvidenceIssuerDetails evidenceIssuer) {
        this.ID = ID;
        this.description = description;
        this.confidentialityLevelCode = confidentialityLevelCode;
        this.evidenceURL = evidenceURL;
        this.evidenceIssuer = evidenceIssuer;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getConfidentialityLevelCode() {
        return confidentialityLevelCode;
    }

    public void setConfidentialityLevelCode(String confidentialityLevelCode) {
        this.confidentialityLevelCode = confidentialityLevelCode;
    }

    public String getEvidenceURL() {
        return evidenceURL;
    }

    public void setEvidenceURL(String evidenceURL) {
        this.evidenceURL = evidenceURL;
    }

    public EvidenceIssuerDetails getEvidenceIssuer() {
        return evidenceIssuer;
    }

    public void setEvidenceIssuer(EvidenceIssuerDetails evidenceIssuer) {
        this.evidenceIssuer = evidenceIssuer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
