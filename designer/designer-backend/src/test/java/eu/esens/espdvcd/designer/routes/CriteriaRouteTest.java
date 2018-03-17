package eu.esens.espdvcd.designer.routes;

import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Assert;
import org.junit.Test;

public class CriteriaRouteTest {

    private final CriteriaRoute criteriaRoute = new CriteriaRoute();
//    @Before
//    public void SetUp() throws Exception{
//      Criteria criteria = new Criteria();
//    }

    @Test
    public void getPredefinedCriteria() throws RetrieverException {
        Assert.assertNotNull(criteriaRoute.getECertisCriteria());
    }

    @Test
    public void getECertisCriteria() throws RetrieverException {
        Assert.assertNotNull(criteriaRoute.getPredefinedCriteria());
    }

}