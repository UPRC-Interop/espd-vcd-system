package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ESPDArtefactResourceTest {

    private ESPDArtefactResource r1;
    private ESPDArtefactResource r2;

    @Before
    public void setUp() {
        r1 = new ESPDArtefactResource(EDMVersion.V1);
        Assert.assertNotNull(r1);
        r2 = new ESPDArtefactResource(EDMVersion.V2);
        Assert.assertNotNull(r2);
    }

    @Test
    public void testGetFullListV1() {
        SelectableCriterionPrinter.print(r1.getCriterionList());
    }

    @Test
    public void testGetFullListV2() {
        SelectableCriterionPrinter.print(r2.getCriterionList());
    }

}
