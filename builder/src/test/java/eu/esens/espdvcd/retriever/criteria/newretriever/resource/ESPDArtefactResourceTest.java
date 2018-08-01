package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ESPDArtefactResourceTest {

    private ESPDArtefactResource artefactResourceV1;
    private ESPDArtefactResource artefactResourceV2;

    @Before
    public void setUp() {
        artefactResourceV1 = new ESPDArtefactResource(SchemaVersion.V1);
        Assert.assertNotNull(artefactResourceV1);
        artefactResourceV2 = new ESPDArtefactResource(SchemaVersion.V2);
        Assert.assertNotNull(artefactResourceV2);
    }

    @Test
    public void testGetFullListV1() {
        printESPDArtefactResource(artefactResourceV1);
    }

    @Test
    public void testGetFullListV2() {
        printESPDArtefactResource(artefactResourceV2);
    }

    private void printESPDArtefactResource(ESPDArtefactResource artefactResource) {

        int index = 1;

        for (SelectableCriterion sc : artefactResource.getCriterionList()) {
            SelectableCriterionPrinter.printSelectableCriterion(sc, index++);
        }

    }

}
