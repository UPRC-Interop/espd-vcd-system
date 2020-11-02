/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import eu.esens.espdvcd.model.util.CustomTextValueDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
                "ID",
                "typeCode",
                "name",
                "description",
                "versionID",
                "feeAmount",
                "evidenceIntendedUse",
                "evidenceIssuerParty",
                "addresseeDescription",
                "jurisdictionLevelCode",
                "evidenceDocumentReference"
        })
public class ECertisEvidenceImpl implements ECertisEvidence {

    private String ID;
    private String typeCode;
    private String name;
    private String description;
    private String versionID;
    private ECertisAmount feeAmount;
    private ECertisEvidenceIntendedUse evidenceIntendedUse;
    private List<ECertisEvidenceIssuerParty> evidenceIssuerParty;
    private String addresseeDescription;
    private List<String> jurisdictionLevelCode;
    private List<ECertisEvidenceDocumentReference> evidenceDocumentReference;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    @JsonProperty("TypeCode")
    public String getTypeCode() {
        return typeCode;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    @Override
    @JsonProperty("VersionID")
    public String getVersionID() {
        return versionID;
    }

    @Override
    @JsonDeserialize(as = ECertisAmountImpl.class)
    public void setFeeAmount(ECertisAmount feeAmount) {
        this.feeAmount = feeAmount;
    }

    @Override
    @JsonProperty("FeeAmount")
    public ECertisAmount getFeeAmount() {
        return feeAmount;
    }

    @Override
    @JsonDeserialize(as = ECertisEvidenceIntendedUseImpl.class)
    public void setEvidenceIntendedUse(ECertisEvidenceIntendedUse evidenceIntendedUse) {
        this.evidenceIntendedUse = evidenceIntendedUse;
    }

    @Override
    @JsonProperty("EvidenceIntendedUse")
    public ECertisEvidenceIntendedUse getEvidenceIntendedUse() {
        return evidenceIntendedUse;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceIssuerPartyImpl.class)
    public void setEvidenceIssuerParty(List<ECertisEvidenceIssuerParty> evidenceIssuerParty) {
        this.evidenceIssuerParty = evidenceIssuerParty;
    }

    @Override
    @JsonProperty("EvidenceIssuerParty")
    public List<ECertisEvidenceIssuerParty> getEvidenceIssuerParty() {
        if (evidenceIssuerParty == null) {
            evidenceIssuerParty = new ArrayList<>();
        }
        return evidenceIssuerParty;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setAddresseeDescription(String addresseeDescription) {
        this.addresseeDescription = addresseeDescription;
    }

    @Override
    @JsonProperty("AddresseeDescription")
    public String getAddresseeDescription() {
        return addresseeDescription;
    }

    @Override
    public void setJurisdictionLevelCode(List<String> jurisdictionLevelCode) {
        this.jurisdictionLevelCode = jurisdictionLevelCode;
    }

    @Override
    @JsonProperty("JurisdictionLevelCode")
    public List<String> getJurisdictionLevelCode() {
        if (jurisdictionLevelCode == null) {
            jurisdictionLevelCode = new ArrayList<>();
        }
        return jurisdictionLevelCode;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceDocumentReferenceImpl.class)
    public void setEvidenceDocumentReference(List<ECertisEvidenceDocumentReference> evidenceDocumentReference) {
        this.evidenceDocumentReference = evidenceDocumentReference;
    }

    @Override
    @JsonProperty("EvidenceDocumentReference")
    public List<ECertisEvidenceDocumentReference> getEvidenceDocumentReference() {
        if (evidenceDocumentReference == null) {
            evidenceDocumentReference = new ArrayList<>();
        }
        return evidenceDocumentReference;
    }

}
