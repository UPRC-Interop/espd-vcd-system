package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

public class CriteriaServiceTest {
    private CriteriaService predefinedCriteriaServiceV1, predefinedCriteriaServiceV2;
    private final static Logger LOGGER = Logger.getLogger(CriteriaServiceTest.class.getName());

    @Before
    public void setUp() throws Exception {
        predefinedCriteriaServiceV1 = new PredefinedCriteriaService(EDMVersion.V1);
        predefinedCriteriaServiceV2 = new PredefinedCriteriaService(EDMVersion.V2);
    }

    @Test
    public void predefinedCriteriaServiceTest() throws RetrieverException {
        List<SelectableCriterion> v1Criteria = predefinedCriteriaServiceV1.getCriteria();
        List<SelectableCriterion> v2Criteria = predefinedCriteriaServiceV2.getCriteria();

        Assert.assertEquals( 63, v1Criteria.size());
        Assert.assertEquals( 63, v2Criteria.size());
    }

}