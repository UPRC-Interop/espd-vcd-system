package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.ruleset.RuleSetFactory;
import eu.esens.espdvcd.model.requirement.ruleset.RuleSet;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ulf Lotzmann on 09/04/2016.
 */
public class RuleSetTest {

    IndicatorResponse response;
    RequirementGroup rgparent = new RequirementGroup("test");

    /**
     * Setup for RuleSet testing.
     */
    @Before
    public void setUp() {

        // setting up indicator
        response = new IndicatorResponse(true);

        // setting up requirement groups
        rgparent = new RequirementGroup("parent");

        // rg with condition related to first rule
        RequirementGroup rgChild1 = new RequirementGroup("child1");
        rgChild1.setCondition("GROUP_FULFILLED.ON_TRUE");
        rgparent.getRequirementGroups().add(rgChild1);

        // rg with condition related to second rule
        RequirementGroup rgChild2 = new RequirementGroup("child2");
        rgChild2.setCondition("GROUP_FULFILLED.ON_FALSE");
        rgparent.getRequirementGroups().add(rgChild2);

        // rg with condition for which no rule is specified
        RequirementGroup rgChild3 = new RequirementGroup("child3");
        rgChild3.setCondition("GROUP_FULFILLED.ON_SOMETHINGELSE");
        rgparent.getRequirementGroups().add(rgChild3);

        // rg without condition
        RequirementGroup rgChild4 = new RequirementGroup("child4");
        rgChild4.setCondition(null);
        rgparent.getRequirementGroups().add(rgChild4);

        // seeting up rule set
        rgparent.setRuleSet(
                RuleSetFactory.createRuleSet(response).
                        addRule(true, "GROUP_FULFILLED.ON_TRUE"). // condition "GROUP_FULFILLED.ON_TRUE" equals value "true" of indicator
                        addRule(false, "GROUP_FULFILLED.ON_FALSE") // condition "GROUP_FULFILLED.ON_FALSE" equals value "false" of indicator
        );
    }

    /**
     * Test of RequirementGroup rules for response indicator dependency.
     */
    @Test()
    public void testIndicatorRuleSet() {
        int condTrue = 0;
        int condFalse = 0;
        int noCond = 0;
        RuleSet ruleSet = rgparent.getRuleSet();

        for (RequirementGroup rg :
                rgparent.getRequirementGroups()) {

            if ((rg.getCondition() != null) && rg.getCondition().equals("GROUP_FULFILLED.ON_TRUE")) {
                // rg with cond.true
                condTrue++;
                response.setIndicator(true);
                assertTrue("Rule evaluation failure - cond.true - true", ruleSet.isRequrementGroupMandatory(rg));
                response.setIndicator(false);
                assertFalse("Rule evaluation failure - cond.true - false", ruleSet.isRequrementGroupMandatory(rg));
            }
            else if ((rg.getCondition() != null) && rg.getCondition().equals("GROUP_FULFILLED.ON_FALSE")) {
                // rg with cond.false
                condFalse++;
                response.setIndicator(true);
                assertFalse("Rule evaluation failure - cond.false - true", ruleSet.isRequrementGroupMandatory(rg));
                response.setIndicator(false);
                assertTrue("Rule evaluation failure - cond.false - false", ruleSet.isRequrementGroupMandatory(rg));
            }
            else {
                // rg with other cond, rg without cond
                noCond++;
                response.setIndicator(true);
                assertTrue("Rule evaluation failure - cond.none - true", ruleSet.isRequrementGroupMandatory(rg));
                response.setIndicator(false);
                assertTrue("Rule evaluation failure - cond.none - false", ruleSet.isRequrementGroupMandatory(rg));
            }

        }
        assertEquals(condTrue, 1); // rg with cond.true just once, if indicator true
        assertEquals(condFalse, 1); // rg with cond.false just once, if indicator false
        assertEquals(noCond, 2); // // rg without cond twice, on any indicator state; but rg with other cond never
    }
}
