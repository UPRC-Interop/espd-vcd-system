/**
 * Copyright 2016-2019 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.esens.espdvcd.codelist.enums.CriterionTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.CriterionPropertyKey;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Criterion
 * <p>
 * <p>
 * Created by Ulf Lotzmann on 05/03/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class Criterion implements Serializable {

    private static final long serialVersionUID = 4541034499184281949L;

    /**
     * Criterion identifier
     * <p>
     * A language-independent token, e.g., a number, that allows to identify a criterion uniquely
     * as well as allows to reference the criterion in other documents. A criterion describes a
     * fact that is used by the contracting body to evaluate and compare tenders by economic
     * operators and which will be used in the award decision.
     * <p>
     * Data type: Identifier<br>
     * Cardinality: 1..1<br>
     * InfReqID: tir70-060, tir92-070<br>
     * BusReqID: tbr70-010, tbr70-009, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.ID<br>
     */
    @NotNull
    protected String ID;

    /**
     * For use by the ESPD Designer
     */
    protected String UUID;

    /**
     * Criterion type
     * <p>
     * Specifies whether the current element is a Criterion or a Subcriterion
     * Uses a subset of the CriterionElementType codelist
     * <p>
     * Data type: Code<br>
     */

    protected CriterionTypeEnum type = CriterionTypeEnum.CRITERION;

    /**
     * Criterion type code
     * <p>
     * Code specifying the type of criterion.
     * <p>
     * Data type: Code<br>
     * Cardinality: 1..n - Remark: strings separated with delimiter<br>
     * InfReqID: tir70-061, tir92-071<br>
     * BusReqID: tbr70-013, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.TypeCode<br>
     */
    @NotNull
    protected String typeCode;

    /**
     * Criterion name
     * <p>
     * A short and descriptive name for a criterion. A criterion describes a fact that is used
     * by the contracting body to evaluate and compare tenders by economic operators and which
     * will be used in the award decision or to assess the eligibility of an economic operator.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-062, tir92-072<br>
     * BusReqID: tbr70-010, tbr70-009, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.Name<br>
     */
    protected String name;

    /**
     * Criterion description
     * <p>
     * An extended description of the criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality:	0..1<br>
     * InfReqID: tir70-063, tir92-073<br>
     * BusReqID: tbr70-010, tbr70-009, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.Description<br>
     */
    protected String description;

    /**
     * Criterion legislation
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path: ccv:Criterion.LegislationReference<br>
     */
    protected LegislationReference legislationReference;

    /**
     * Requirement group List
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup<br>
     */
    protected List<RequirementGroup> requirementGroups;

    /**
     * The Sub-Criterion List contains the National Criteria
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path:<br>
     */
    protected List<SelectableCriterion> subCriterionList;

    /**
     * The property keys map contains the criterion's property keys,
     * which are used for multilinguality purposes. Currently there are 2
     * property keys at Criterion level, 1 for Criterion's Name and 1 for
     * Criterion's Description.
     * <p>
     * Data type: Text<br>
     * Cardinality: 1<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path:<br>
     */
    protected Map<String, String> propertyKeyMap;

    /**
     * The Evidence list contains the criterion's evidences.
     * <p>
     * Data type: Class<br>
     * Cardinality: 0..n<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path:<br>
     */
    protected List<Evidence> evidenceList;

    /**
     * Work as a cascade switch.
     * <p>
     * Data type: Boolean<br>
     * Cardinality: 1<br>
     * InfReqID:<br>
     * BusReqID:<br>
     * UBL syntax path:<br>
     */
    protected boolean useKeyAsValue = false;

    public Criterion() {
        this.ID = java.util.UUID.randomUUID().toString();
    }

    public Criterion(String ID, String typeCode, CriterionTypeEnum type, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.type = type;
        this.name = name;
        this.description = description;
        this.legislationReference = legislationReference;
        this.requirementGroups = requirementGroups;
    }

    public Criterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.description = description;
        this.legislationReference = legislationReference;
        this.requirementGroups = requirementGroups;
    }

    public Criterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.description = description;
        this.legislationReference = legislationReference;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public CriterionTypeEnum getType() {
        return type;
    }

    public void setType(CriterionTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return useKeyAsValue ? getPropertyKeyOrNull(CriterionPropertyKey.NAME) : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return useKeyAsValue ? getPropertyKeyOrNull(CriterionPropertyKey.DESCRIPTION) : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SelectableCriterion> getSubCriterionList() {
        if (subCriterionList == null) {
            subCriterionList = new ArrayList<>();
        }
        return subCriterionList;
    }

    public void setSubCriterionList(List<SelectableCriterion> subCriterionList) {
        this.subCriterionList = subCriterionList;
    }

    public List<RequirementGroup> getRequirementGroups() {
        if (requirementGroups == null) {
            requirementGroups = new ArrayList<>();
        }
        return requirementGroups;
    }

    public void setRequirementGroups(List<RequirementGroup> requirementGroups) {
        this.requirementGroups = requirementGroups;
    }

    public LegislationReference getLegislationReference() {
        return legislationReference;
    }

    public void setLegislationReference(LegislationReference legislationReference) {
        this.legislationReference = legislationReference;
    }


    public String getCriterionGroup() {
        String[] ar = this.typeCode.split("\\.", 4);
        StringBuilder sb = new StringBuilder();
        int size = 3;
        if (ar.length < size) size = ar.length;
        for (int i = 0; i < size; i++) {
            sb.append(ar[i]);
            sb.append(".");
        }
        return sb.delete(sb.lastIndexOf("."), sb.length()).toString();
    }

    public Map<String, String> getPropertyKeyMap() {
        if (propertyKeyMap == null) {
            propertyKeyMap = new HashMap<>();
        }
        return propertyKeyMap;
    }

    public String getPropertyKeyOrNull(CriterionPropertyKey key) {
        return propertyKeyMap.get(key.getCode().getValue());
    }

    public List<Evidence> getEvidenceList() {
        if (evidenceList == null) {
            evidenceList = new ArrayList<>();
        }
        return evidenceList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.ID);
        hash = 29 * hash + Objects.hashCode(this.typeCode);
        hash = 29 * hash + Objects.hashCode(this.name);
        // hash = 29 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Criterion other = (Criterion) obj;

        return Objects.equals(this.ID, other.ID);
    }

    public void setKeyAsResponseValue(boolean useKeyAsValue) {
        this.useKeyAsValue = useKeyAsValue;
    }

}
