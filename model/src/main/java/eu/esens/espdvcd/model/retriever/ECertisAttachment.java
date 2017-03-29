package eu.esens.espdvcd.model.retriever;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisAttachment {
    
    void setExternalReference(ECertisExternalReference externalReference);
    
    ECertisExternalReference getExternalReference();
    
}
