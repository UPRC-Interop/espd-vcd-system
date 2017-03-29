package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractorTest {

    static int maxDepth = 1;
    private ECertisCriteriaExtractor extractor;
   
    @Before
    public void setUp() {
        extractor = new ECertisCriteriaExtractor();
    }
   
    /**
     * Test of getFullList method, of class ECertisCriteriaExtractor.
     */
    @Test
    public void testGetFullList() {
        try {
            extractor.getFullList().stream()
                    .forEach( (SelectableCriterion sc) -> {
                        System.out.println(sc.getID() + " " + sc.getName() + " (" + sc.getTypeCode() + ")");
                        sc.getRequirementGroups().forEach(rg -> traverseRequirementGroup(rg, 1));
                    });
            System.out.println("Max Depth: " + maxDepth);
            System.out.println("Criterion #: " + extractor.getFullList().size());
        } catch (RetrieverException ex) {
            System.err.println(ex);
        }
    }

    private void traverseRequirementGroup(RequirementGroup rg, int depth) {

        if (depth > maxDepth) {
            maxDepth = depth;
        }

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs + "RequirementGroup: " + rg.getID());
        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> traverseRequirementGroup(rg1, innerDepth));
    }
    
}
