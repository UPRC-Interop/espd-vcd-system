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
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText name;
    @JsonDeserialize(as = ECertisText.class)
    private IECertisText description;
    private String versionID;
    @JsonDeserialize(as = ECertisAmountType.class)
    private IECertisAmountType feeAmount;
    @JsonDeserialize(as = ECertisEvidenceIntendedUse.class)
    private IECertisEvidenceIntendedUse evidenceIntendedUse;
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceIssuerParty.class)
    private List<IECertisEvidenceIssuerParty> evidenceIssuerParty;
    private String addresseeDescription;
    private List<String> jurisdictionLevelCode;
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceDocumentReference.class)
    private List<IECertisEvidenceDocumentReference> evidenceDocumentReference;

    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    @JsonProperty("ID")
    public String getID() {
        return ID;
    }

    @Override
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    @JsonProperty("TypeCode")
    public String getTypeCode() {
        return typeCode;
    }

    @Override
    public void setName(IECertisText name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    public IECertisText getName() {
        return name;
    }

    @Override
    public void setDescription(IECertisText description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    public IECertisText getDescription() {
        return description;
    }

    @Override
    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    @Override
    @JsonProperty("VersionID")
    public String getVersionID() {
        return versionID;
    }

    @Override
    public void setFeeAmount(IECertisAmountType feeAmount) {
        this.feeAmount = feeAmount;
    }

    @Override
    @JsonProperty("FeeAmount")
    public IECertisAmountType getFeeAmount() {
        return feeAmount;
    }
    
    @Override
    public void setEvidenceIntendedUse(IECertisEvidenceIntendedUse evidenceIntendedUse) {
        this.evidenceIntendedUse = evidenceIntendedUse;
    }

    @Override
    @JsonProperty("EvidenceIntendedUse")
    public IECertisEvidenceIntendedUse getEvidenceIntendedUse() {
        return evidenceIntendedUse;
    }

    @Override
    public void setEvidenceIssuerParty(List<IECertisEvidenceIssuerParty> evidenceIssuerParty) {
        this.evidenceIssuerParty = evidenceIssuerParty;
    }

    @Override
    @JsonProperty("EvidenceIssuerParty")
    public List<IECertisEvidenceIssuerParty> getEvidenceIssuerParty() {
        return evidenceIssuerParty;
    }
        
    
    @Override
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
        return jurisdictionLevelCode;
    }

    @Override
    public void setEvidenceDocumentReference(List<IECertisEvidenceDocumentReference> evidenceDocumentReference) {
        this.evidenceDocumentReference = evidenceDocumentReference;
    }

    @Override
    @JsonProperty("EvidenceDocumentReference")
    public List<IECertisEvidenceDocumentReference> getEvidenceDocumentReference() {
        return evidenceDocumentReference;
    }
        
}
