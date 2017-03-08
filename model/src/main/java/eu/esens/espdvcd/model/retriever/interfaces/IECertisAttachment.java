package eu.esens.espdvcd.model.retriever.interfaces;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisAttachment {
    
    void setExternalReference(IECertisExternalReference externalReference);
    
    IECertisExternalReference getExternalReference();
    
}
