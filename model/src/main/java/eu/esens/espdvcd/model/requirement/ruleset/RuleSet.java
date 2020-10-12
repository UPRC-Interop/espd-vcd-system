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
package eu.esens.espdvcd.model.requirement.ruleset;

import eu.esens.espdvcd.model.requirement.RequirementGroup;

/**
 * Rule set interface that links a Response object (called indicator in this context) to
 * RequirementGroup objects and allows to evaluate the state of the requirement groups
 * ("mandatory" / "not mandatory") with respect to the indicator state.
 *
 * The defined rules provide the mapping between conditions for requirement groups and
 * values of the response object .
 *
 * Created by Ulf Lotzmann on 07/04/2016.
 */
public interface RuleSet<IndicatorType> {

    /**
     * Enum with operators used during rule evaluation (typically compare operators). Default operator is EQUALS.
     */
    enum RuleTypes {
        EQUALS,
        LESS_THAN,
        GREATER_THAN
    }

    /**
     * Adds a rule to the rule set.
     * @param type The (compare) operator of th rule.
     * @param indictorStatus The value that should be matched with the condition from the Response side.
     * @param condition The value that should be matched with the indicatorStatus from the RequirementGroup condition side.
     * @return The actual rule set object.
     */
    RuleSet addRule(RuleTypes type, IndicatorType indictorStatus, String condition);

    /**
     * Default method for adding a rule using the EQUALS operator.
     * @param indictorStatus The value that should be matched with the condition from the Response side.
     * @param condition The value that should be matched with the indicatorStatus from the RequirementGroup condition side.
     * @return The actual rule set object.
     */
    default RuleSet addRule(IndicatorType indictorStatus, String condition) {
        return addRule(RuleTypes.EQUALS ,indictorStatus, condition);
    }

    /**
     * Rule execution.
     * @param rg The requirement group to check against the current state of the assigned indicator.
     * @return TRUE, if given requirement group is mandatory with the current indicator status, or no rule is specified.
     */
    boolean isRequrementGroupMandatory(RequirementGroup rg);
}
