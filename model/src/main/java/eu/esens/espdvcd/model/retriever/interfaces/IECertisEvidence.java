package eu.esens.espdvcd.model.retriever.interfaces;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisEvidence {
    
    void setID(String ID);
    
    String getID();
    
    void setTypeCode(String typeCode);
    
    String getTypeCode();
    
    void setName(IECertisText name);
    
    IECertisText getName();
    
    void setDescription(IECertisText description);
    
    IECertisText getDescription();
    
    void setVersionID(String versionID);
    
    String getVersionID();
    
    // NOT SURE HERE ABOUT THE TYPE
    void setFeeAmount(IECertisText feeAmount);
    
    // NOT SURE HERE ABOUT THE TYPE
    IECertisText getFeeAmount();
    
    void setEvidenceIntendedUse(IECertisEvidenceIntendedUse evidenceIntendedUse);
    
    IECertisEvidenceIntendedUse getEvidenceIntendedUse();
        
    void setEvidenceIssuerParty(List<IECertisEvidenceIssuerParty> evidenceIssuerParty);
    
    List<IECertisEvidenceIssuerParty> getEvidenceIssuerParty();
    
    void setAddresseeDescription(String addresseeDescription);
    
    String getAddresseeDescription();
    
    void setJurisdictionLevelCode(List<String> jurisdictionLevelCode);
    
    List<String> getJurisdictionLevelCode();
    
    void setEvidenceDocumentReference(List<IECertisEvidenceDocumentReference> evidenceDocumentReference);
    
    List<IECertisEvidenceDocumentReference> getEvidenceDocumentReference();
    
}
