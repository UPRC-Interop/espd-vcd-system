package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CriteriaTaxonomyResourceTest {

    private CriteriaTaxonomyResource taxonomyResource;

    @Before
    public void setUp() {
        taxonomyResource = new CriteriaTaxonomyResource();
        Assert.assertNotNull(taxonomyResource);
    }

    @Test
    public void testGetFullList() {

        taxonomyResource.getCriterionList().forEach(sc -> {
            System.out.println("ID: " + sc.getID() + " Name: " + sc.getName() + " Desc: " + sc.getDescription() + " Element Code: " + sc.getTypeCode());
            System.out.println("Legislation: " + sc.getLegislationReference());
            sc.getRequirementGroups().forEach(rqg -> printRequirementGroup(rqg, 3));
            System.out.println();
        });

    }

    private void printRequirementGroup(RequirementGroup rg, int depth) {

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs + "RequirementGroup: " + rg.getID() + " Condition: " + rg.getCondition());
        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> printRequirementGroup(rg1, innerDepth));
    }

}
