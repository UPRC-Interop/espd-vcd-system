/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomLegislationReferenceDeserializer;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;
import eu.esens.espdvcd.model.util.CustomTextValueDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "ID",
            "typeCode",
            "name",
            "description",
            "domainID",
            "versionID",
            "legislationReference",
            "evidenceGroups",
            "subCriterions",
            "parentCriterion"
        })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ECertisCriterionImpl implements ECertisCriterion {

    private String ID;
    private String typeCode;
    private String name;
    private String description;
    private String domainID;
    private String versionID;
    private ECertisLegislationReference legislationReference;
    private List<ECertisEvidenceGroup> evidenceGroups;
    private List<ECertisCriterion> subCriteria;
    private ECertisCriterion parentCriterion;

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    @JsonProperty("ID")
    @JsonAlias({"id", "Id"})
    public String getID() {
        return ID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    @JsonProperty("TypeCode")
    @JsonAlias({"typeCode"})
    public String getTypeCode() {
        return typeCode;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonProperty("Name")
    @JsonAlias({"name"})
    public String getName() {
        return name;
    }

    @Override
    @JsonDeserialize(using = CustomTextValueDeserializer.class)
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty("Description")
    @JsonAlias({"description"})
    public String getDescription() {
        return description;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    @Override
    @JsonProperty("DomainID")
    @JsonAlias({"domainID"})
    public String getDomainID() {
        return domainID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    @Override
    @JsonProperty("VersionID")
    @JsonAlias({"versionID"})
    public String getVersionID() {
        return versionID;
    }

    @Override
    @JsonProperty("LegislationReference")
    @JsonAlias({"legislationReference"})
    public ECertisLegislationReference getLegislationReference() {
        return legislationReference;
    }
        
    @Override
    @JsonSetter("LegislationReference")
    @JsonAlias({"legislationReference"})
    @JsonDeserialize(as = ECertisLegislationReferenceImpl.class, using = CustomLegislationReferenceDeserializer.class)
    public void setLegislationReference(ECertisLegislationReference legislationReference) {
        this.legislationReference = legislationReference;
    }
   
    @Override
    @JsonSetter("RequirementGroup")
    @JsonAlias({"requirementGroup"})
    @JsonDeserialize(as = List.class, contentAs = ECertisEvidenceGroupImpl.class)
    public void setEvidenceGroups(List<ECertisEvidenceGroup> evidenceGroups) {
        this.evidenceGroups = evidenceGroups;
    }

    @Override
    @JsonProperty("RequirementGroup")
    @JsonAlias({"requirementGroup"})
    public List<ECertisEvidenceGroup> getEvidenceGroups() {
        if (evidenceGroups == null) {
            evidenceGroups = new ArrayList<>();
        }
        return evidenceGroups;
    }

    @Override
    @JsonSetter("SubCriterion")
    @JsonAlias({"subCriterion"})
    @JsonDeserialize(as = List.class, contentAs = ECertisCriterionImpl.class)
    public void setSubCriterions(List<ECertisCriterion> subCriterions) {
        this.subCriteria = subCriterions;
    }

    @Override
    @JsonProperty("SubCriterion")
    @JsonAlias({"subCriterion"})
    public List<ECertisCriterion> getSubCriteria() {
        if (subCriteria == null) {
            subCriteria = new ArrayList<>();
        }
        return subCriteria;
    }

    @Override
    @JsonSetter("ParentCriterion")
    @JsonAlias({"parentCriterion"})
    @JsonDeserialize(as = ECertisCriterionImpl.class)
    public void setParentCriterion(ECertisCriterion parentCriterion) {
        this.parentCriterion = parentCriterion;
    }

    @Override
    @JsonProperty("ParentCriterion")
    @JsonAlias({"parentCriterion"})
    public ECertisCriterion getParentCriterion() {
        return parentCriterion;
    }

}
