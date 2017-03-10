package eu.esens.espdvcd.model.retriever.interfaces;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisEvidenceDocumentReference {
    
    void setAttachment(IECertisAttachment attachment);
    
    IECertisAttachment getAttachment();
    
    void setValidityPeriod(IECertisValidityPeriod validityPeriod);
    
    IECertisValidityPeriod getValidityPeriod();
    
    void setIssuerParty(IECertisIssuerParty issuerParty);
    
    IECertisIssuerParty getIssuerParty();
    
}
