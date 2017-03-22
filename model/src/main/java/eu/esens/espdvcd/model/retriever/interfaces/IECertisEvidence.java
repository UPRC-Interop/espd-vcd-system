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

//    void setName(IECertisText name);
//
//    IECertisText getName();

    void setName(String name);
    
    String getName();
    
//    void setDescription(IECertisText description);
//
//    IECertisText getDescription();

    void setDescription(String description);
    
    String getDescription();
    
    void setVersionID(String versionID);

    String getVersionID();

    void setFeeAmount(IECertisAmountType feeAmount);
        
    IECertisAmountType getFeeAmount();
    
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
