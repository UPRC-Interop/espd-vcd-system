package eu.esens.espdvcd.model.retriever.interfaces;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisValidityPeriod {
    
    void setDescription(List<IECertisText> description);
    
    List<IECertisText> getDescription();
    
}
