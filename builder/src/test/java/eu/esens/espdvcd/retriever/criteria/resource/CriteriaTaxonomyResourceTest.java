package eu.esens.espdvcd.retriever.criteria.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CriteriaTaxonomyResourceTest {

    private CriteriaTaxonomyResource r;

    @Before
    public void setUp() {
        r = new CriteriaTaxonomyResource();
        Assert.assertNotNull(r);
    }

    @Test
    public void testGetFullList() {
        SelectableCriterionPrinter.print(r.getCriterionList());
    }

}
