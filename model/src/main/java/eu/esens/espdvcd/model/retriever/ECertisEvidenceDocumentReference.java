package eu.esens.espdvcd.model.retriever;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisEvidenceDocumentReference {
    
    void setAttachment(ECertisAttachment attachment);
    
    ECertisAttachment getAttachment();
    
    void setValidityPeriod(ECertisValidityPeriod validityPeriod);
    
    ECertisValidityPeriod getValidityPeriod();
    
    void setIssuerParty(ECertisIssuerParty issuerParty);
    
    ECertisIssuerParty getIssuerParty();
    
}
