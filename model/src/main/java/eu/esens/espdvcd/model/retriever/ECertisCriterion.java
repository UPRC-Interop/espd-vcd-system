/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.retriever;

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

    ECertisLegislationReference getLegislationReference();

    void setLegislationReference(ECertisLegislationReference legislationReference);
        
    void setEvidenceGroups(List<ECertisEvidenceGroup> evidenceGroups);
    
    List<ECertisEvidenceGroup> getEvidenceGroups();
    
    void setSubCriterions(List<ECertisCriterion> subCriterions);
    
    List<ECertisCriterion> getSubCriterions();
    
    void setParentCriterion(ECertisCriterion parentCriterion);
    
    ECertisCriterion getParentCriterion();
    
}
