package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisAmountType;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidence;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceIntendedUse;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisText;
import java.util.List;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceDocumentReference;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceIssuerParty;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import java.util.ArrayList;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder( {"ID", "typeCode", "name", "description", "versionID", "feeAmount",
"evidenceIntendedUse", "evidenceIssuerParty", "addresseeDescription", "jurisdictionLevelCode", 
"evidenceDocumentReference"} )
public class ECertisEvidence implements IECertisEvidence {
    
    private String ID;
    private String typeCode;
    private IECertisText name;
    private IECertisText description;
    private String versionID;
    private IECertisAmountType feeAmount;
    private IECertisEvidenceIntendedUse evidenceIntendedUse;
    private List<IECertisEvidenceIssuerParty> evidenceIssuerParty;
    private String addresseeDescription;
    private List<String> jurisdictionLevelCode;
    private List<IECertisEvidenceDocumentReference> evidenceDocumentReference;

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
    @JsonDeserialize(as = ECertisText.class)
    public void setName(IECertisText name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    public IECertisText getName() {
        return name;
    }

    @Override
    @JsonDeserialize(as = ECertisText.class)
    public void setDescription(IECertisText description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public IECertisText getDescription() {
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
    @JsonDeserialize(as = ECertisAmountType.class)
    public void setFeeAmount(IECertisAmountType feeAmount) {
        this.feeAmount = feeAmount;
    }

    @Override
    @JsonProperty("FeeAmount")
    public IECertisAmountType getFeeAmount() {
        return feeAmount;
    }
    
    @Override
    @JsonDeserialize(as = ECertisEvidenceIntendedUse.class)
    public void setEvidenceIntendedUse(IECertisEvidenceIntendedUse evidenceIntendedUse) {
        this.evidenceIntendedUse = evidenceIntendedUse;
    }

    @Override
    @JsonProperty("EvidenceIntendedUse")
    public IECertisEvidenceIntendedUse getEvidenceIntendedUse() {
        return evidenceIntendedUse;
    }

    @Override
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceIssuerParty.class)
    public void setEvidenceIssuerParty(List<IECertisEvidenceIssuerParty> evidenceIssuerParty) {
        if (evidenceIssuerParty == null) {
            evidenceIssuerParty = new ArrayList<>();
        }
        this.evidenceIssuerParty = evidenceIssuerParty;
    }

    @Override
    @JsonProperty("EvidenceIssuerParty")
    public List<IECertisEvidenceIssuerParty> getEvidenceIssuerParty() {
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
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceDocumentReference.class)
    public void setEvidenceDocumentReference(List<IECertisEvidenceDocumentReference> evidenceDocumentReference) {
        this.evidenceDocumentReference = evidenceDocumentReference;
    }

    @Override
    @JsonProperty("EvidenceDocumentReference")
    public List<IECertisEvidenceDocumentReference> getEvidenceDocumentReference() {
        if (evidenceDocumentReference == null) {
            evidenceDocumentReference = new ArrayList<>();
        }
        return evidenceDocumentReference;
    }
        
}
