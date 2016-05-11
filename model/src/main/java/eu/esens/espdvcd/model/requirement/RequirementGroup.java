package eu.esens.espdvcd.model.requirement;

import eu.esens.espdvcd.model.requirement.ruleset.RuleSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Criterion requirement group
 *
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
     * InfReqID: <br>
     * BusReqID: tbr70-013, tbr70-004<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.ID<br>
     */
    private String ID;

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
    private List<Requirement> requirements;

    /**
     * Criterion requirement group
     * <p>
     *
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



    public RequirementGroup(String ID) {
        this.ID = ID;
    }
    
    public RequirementGroup(String ID, List<Requirement> requirements) {
        this.ID = ID;
        this.requirements = requirements;
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
}
