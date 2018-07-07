package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.newretriever.CriteriaExtractorBuilderImpl;
import eu.esens.espdvcd.retriever.criteria.newretriever.CriteriaTaxonomyResource;
import org.junit.Before;
import org.junit.Test;

public class NewCriteriaExtractorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testNewCriteriaExtractorBuilder() throws Exception {

        CriteriaTaxonomyResource taxonomyResource = new CriteriaTaxonomyResource();

        CriteriaExtractor extractor = new CriteriaExtractorBuilderImpl()
                .withCriteriaResource(taxonomyResource)
                .withLegislationResource(null)
                .withRequirementGroupsResource(taxonomyResource)
                .build();

        extractor.getFullList();
    }

}
