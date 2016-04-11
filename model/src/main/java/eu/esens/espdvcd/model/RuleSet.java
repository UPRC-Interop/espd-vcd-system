package eu.esens.espdvcd.model;

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
