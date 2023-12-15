package eu.esens.espdvcd.retriever.criteria.filters;

import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 12/11/2020.
 */
public class CriterionFiltersTest {

    final private CriteriaTaxonomyResource taxonomyResourceV2 = new RegulatedCriteriaTaxonomyResource();

    @Before
    public void setUp() {

    }

    @Test
    public void testProvidedByECertisPredicate() {
        Assert.assertEquals(56, taxonomyResourceV2.getCriterionList().stream()
                .filter(CriterionFilters.isProvidedByECertis())
                .collect(Collectors.toList()).size());
    }


}
