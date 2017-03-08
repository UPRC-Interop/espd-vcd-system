package eu.esens.espdvcd.model.retriever.interfaces;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IECertisCriterion {
    
    void setID(String ID);
    
    String getID();
    
    void setTypeCode(String typeCode);
    
    String getTypeCode();
    
    void setName(IECertisText name);
    
    IECertisText getName();
    
    void setDomainID(String domainID);
    
    String getDomainID();
    
    void setVersionID(String versionID);
    
    String getVersionID();
    
    void setLegislationReference(List<IECertisLegislationReference> legislationReference);
    
    List<IECertisLegislationReference> getLegislationReference();
    
    void setEvidenceGroup(List<IECertisEvidenceGroup> evidenceGroup);
    
    List<IECertisEvidenceGroup> getEvidenceGroup();
  
    void setParentCriterion(IECertisCriterion parentCriterion);
    
    IECertisCriterion getParentCriterion();
    
}
