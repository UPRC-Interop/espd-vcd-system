package eu.esens.espdvcd.model.retriever;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisEvidence {

    void setID(String ID);

    String getID();

    void setTypeCode(String typeCode);

    String getTypeCode();

    void setName(String name);
    
    String getName();

    void setDescription(String description);

    String getDescription();
   
    void setVersionID(String versionID);

    String getVersionID();

    void setFeeAmount(ECertisAmount feeAmount);
        
    ECertisAmount getFeeAmount();
    
    void setEvidenceIntendedUse(ECertisEvidenceIntendedUse evidenceIntendedUse);

    ECertisEvidenceIntendedUse getEvidenceIntendedUse();

    void setEvidenceIssuerParty(List<ECertisEvidenceIssuerParty> evidenceIssuerParty);

    List<ECertisEvidenceIssuerParty> getEvidenceIssuerParty();

    void setAddresseeDescription(String addresseeDescription);

    String getAddresseeDescription();

    void setJurisdictionLevelCode(List<String> jurisdictionLevelCode);

    List<String> getJurisdictionLevelCode();

    void setEvidenceDocumentReference(List<ECertisEvidenceDocumentReference> evidenceDocumentReference);

    List<ECertisEvidenceDocumentReference> getEvidenceDocumentReference();

}
