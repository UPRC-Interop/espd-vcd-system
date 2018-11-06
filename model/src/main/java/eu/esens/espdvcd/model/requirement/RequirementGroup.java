/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.model.requirement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.codelist.enums.RequirementGroupTypeEnum;
import eu.esens.espdvcd.model.requirement.ruleset.RuleSet;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Criterion requirement group
 * <p>
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroup implements Serializable {

    private static final long serialVersionUID = -2179415621032376712L;

    /**
     * Criterion requirement group identifier
     * <p>
     * An identifier that allows to identify a group of requirements uniquely.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir70-320, tir92-320<br>
     * BusReqID: tbr70-013, tbr70-004, tbr92-015, tbr92-016<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.ID<br>
     */
    private String ID;

    private String UUID;

    /**
     * Criterion requirements
     * <p>
     * Requirements to fulfill an specific criterion.
     * <p>
     * Data type: <br>
     * Cardinality: 1..n<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement<br>
     */
    @NotNull
    @Size(min = 1)
    private List<Requirement> requirements;

    /**
     * Criterion requirement group
     * <p>
     * <p>
     * <p>
     * Data type: <br>
     * Cardinality: 0..n<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup<br>
     */
    private List<RequirementGroup> requirementGroups;

    // data elements for business rules
    private RuleSet ruleSet;

    // condition for making this requirement group mandatory, e.g. GROUP_FULFILLED.ON_TRUE, GROUP_FULFILLED.ON_FALSE
    private String condition;

    private RequirementGroupTypeEnum type = RequirementGroupTypeEnum.QUESTION_GROUP;

    private boolean mandatory;

    private boolean multiple;

    public RequirementGroup(String ID) {
        this.ID = ID;
        // apply default cardinality 1
        this.mandatory = true;
        this.multiple = false;
    }

    public RequirementGroup(String ID,
                            List<Requirement> requirements) {
        this.ID = ID;
        this.requirements = requirements;
        // apply default cardinality 1
        this.mandatory = true;
        this.multiple = false;
    }

    @JsonCreator
    public RequirementGroup(@JsonProperty("ID") String ID,
                            @JsonProperty("requirements") List<Requirement> requirements,
                            @JsonProperty("type") RequirementGroupTypeEnum type) {
        this.ID = ID;
        this.requirements = requirements;
        this.type = type;
        // apply default cardinality 1
        this.mandatory = true;
        this.multiple = false;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<Requirement> getRequirements() {
        if (requirements == null) {
            requirements = new ArrayList<>();
        }
        return requirements;
    }

    public void setRequirements(List<Requirement> requirement) {
        this.requirements = requirement;
    }

    public void setRequirementGroups(List<RequirementGroup> childRg) {
        this.requirementGroups = childRg;
    }

    public List<RequirementGroup> getRequirementGroups() {
        if (this.requirementGroups == null) {
            this.requirementGroups = new ArrayList<>();
        }
        return this.requirementGroups;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public RequirementGroupTypeEnum getType() {
        return type;
    }

    public void setType(RequirementGroupTypeEnum type) {
        this.type = type;
    }

}
