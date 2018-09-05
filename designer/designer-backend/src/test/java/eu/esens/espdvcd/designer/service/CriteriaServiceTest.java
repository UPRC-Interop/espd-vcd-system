package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

public class CriteriaServiceTest {
    private CriteriaService predefinedCriteriaServiceV1, predefinedCriteriaServiceV2;
    private CriteriaExtractor criteriaExtractorV1, criteriaExtractorV2;

    @Before
    public void setUp() throws Exception {
        predefinedCriteriaServiceV1 = new RetrieverCriteriaService(EDMVersion.V1);
        predefinedCriteriaServiceV2 = new RetrieverCriteriaService(EDMVersion.V2);

        criteriaExtractorV1 = new CriteriaExtractorBuilder(EDMVersion.V1).build();
        criteriaExtractorV2 = new CriteriaExtractorBuilder(EDMVersion.V2).build();
    }

    @Test
    public void testV1Criteria() throws RetrieverException {
        Assert.assertArrayEquals(predefinedCriteriaServiceV1.getCriteria().toArray(), criteriaExtractorV1.getFullList().toArray());
    }

    @Test
    public void testV2Criteria() throws RetrieverException {
        Assert.assertArrayEquals(predefinedCriteriaServiceV2.getCriteria().toArray(), criteriaExtractorV2.getFullList().toArray());
    }

    @Test
    public void getUnselectedCriteriaV2() throws Exception {
        List<SelectableCriterion> theList = predefinedCriteriaServiceV2.getCriteria();
        theList.remove(30);
        List<SelectableCriterion> theListWithMissing30 = predefinedCriteriaServiceV2.getUnselectedCriteria(theList);
        Assert.assertFalse(theListWithMissing30.get(theListWithMissing30.size() - 1).isSelected());
//        System.out.println(theListWithMissing30.size());
    }

    @Test
    public void getUnselectedCriteriaV1() throws Exception {
        List<SelectableCriterion> theList = predefinedCriteriaServiceV1.getCriteria();
        theList.remove(30);
        List<SelectableCriterion> theListWithMissing30 = predefinedCriteriaServiceV1.getUnselectedCriteria(theList);
        Assert.assertFalse(theListWithMissing30.get(theListWithMissing30.size() - 1).isSelected());
//        System.out.println(theListWithMissing30.size());
    }

}