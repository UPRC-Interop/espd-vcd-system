package eu.esens.espdvcd.designer.routes;

import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CriteriaTest {

    private final Criteria criteria = new Criteria();
//    @Before
//    public void SetUp() throws Exception{
//      Criteria criteria = new Criteria();
//    }

    @Test
    public void getPredefinedCriteria() throws RetrieverException {
        Assert.assertNotNull(criteria.getECertisCriteria());
    }

    @Test
    public void getECertisCriteria() throws RetrieverException {
        Assert.assertNotNull(criteria.getPredefinedCriteria());
    }
}