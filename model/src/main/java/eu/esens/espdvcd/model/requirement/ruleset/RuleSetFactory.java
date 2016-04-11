package eu.esens.espdvcd.model.requirement.ruleset;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating RuleSet objects for the following Response classes as indicators:
 *  - IndicatorResponse
 *
 * Created by Ulf Lotzmann on 07/04/2016.
 */
public class RuleSetFactory {

    /**
     * Creates a RuleSet object for the specified Response object
     * @param indicator The Response object that should be linked to the RuleSet.
     * @return The RuleSet object.
     */
    public static RuleSet createRuleSet(Response indicator) {
        if (indicator instanceof IndicatorResponse) {
            // create rule set for IndicatorResponse
            return new ResponseIndicatorRuleSet((IndicatorResponse)indicator);
        }
        return null;
    }


    private static class ResponseIndicatorRuleSet implements RuleSet<Boolean> {

        IndicatorResponse indicator;

        Map<String, Boolean> rules = new HashMap<>();

        public ResponseIndicatorRuleSet(IndicatorResponse indicator) {
            this.indicator = indicator;
        }

        /**
         * Adds a rule to the rule set.
         *
         * @param type           The (compare) operator of th rule.
         * @param indictorStatus The value that should be matched with the condition from the Response side.
         * @param condition      The value that should be matched with the indicatorStatus from the RequirementGroup condition side.
         * @return The actual rule set object.
         */
        @Override
        public RuleSet addRule(RuleTypes type, Boolean indictorStatus, String condition) {
            rules.put(condition, indictorStatus);
            return this;
        }

        /**
         * Rule execution.
         *
         * @param rg The requirement group to check against the current state of the assigned indicator.
         * @return TRUE, if given requirement is mandatory with the current indicator status, or no rule or indicator is specified.
         */
        @Override
        public boolean isRequrementGroupMandatory(RequirementGroup rg) {
            if (rg == null) return false;

            // compare rg.condition with rules
            Boolean cond = rules.get(rg.getCondition());

            return cond==null || cond.equals(indicator.isIndicator());
        }
    }

}
