package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.LegislationReference;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisCriterion {
    
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
    
    void setLegislationReference(LegislationReference legislationReference);
        
    LegislationReference getLegislationReference();
        
    void setEvidenceGroups(List<ECertisEvidenceGroup> evidenceGroups);
    
    List<ECertisEvidenceGroup> getEvidenceGroups();
    
    void setSubCriterions(List<ECertisCriterion> subCriterions);
    
    List<ECertisCriterion> getSubCriterions();
    
    void setParentCriterion(ECertisCriterion parentCriterion);
    
    ECertisCriterion getParentCriterion();
    
}