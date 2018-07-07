package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.newretriever.CriteriaExtractorImpl;
import eu.esens.espdvcd.retriever.criteria.newretriever.ExcelResource;
import org.junit.Before;
import org.junit.Test;

public class NewCriteriaExtractorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testNewCriteriaExtractorBuilder() throws Exception {

        ExcelResource resource = new ExcelResource();

        CriteriaExtractorImpl extractor = new CriteriaExtractorImpl.Builder()
                .withCriterionResource(resource)
                .withLegislationResource(null)
                .withRequirementGroupResource(resource)
                .build();

        extractor.getFullList();
    }

}
