package eu.esens.espdvcd.model.retriever.interfaces;

import eu.esens.espdvcd.model.LegislationReference;
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
    
    void setName(String name);
    
    String getName();
    
    void setDescription(String description);
    
    String getDescription();
    
    void setDomainID(String domainID);
    
    String getDomainID();
    
    void setVersionID(String versionID);
    
    String getVersionID();
    
    void setTheLegislationReference(List<IECertisLegislationReference> legislationReference);
        
    List<IECertisLegislationReference> getTheLegislationReference();
        
    void setEvidenceGroup(List<IECertisEvidenceGroup> evidenceGroup);
    
    List<IECertisEvidenceGroup> getEvidenceGroup();
    
    void setSubCriterion(List<IECertisCriterion> subCriterion);
    
    List<IECertisCriterion> getSubCriterion();
    
    void setParentCriterion(IECertisCriterion parentCriterion);
    
    IECertisCriterion getParentCriterion();
    
}
