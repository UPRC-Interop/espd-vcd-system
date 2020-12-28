package eu.esens.espdvcd.retriever.criteria.resource.tasks;

import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.retriever.criteria.filters.CriterionFilters;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.utils.ECertisURIBuilder;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 28/12/2020.
 */
public class GetFromECertisTaskTest {

    @Ignore
    @Test
    public void testAllEuropeanCriteriaResponseTime() throws Exception {

        CriteriaTaxonomyResource taxonomyResourceV2 = new RegulatedCriteriaTaxonomyResource();

        List<String> initialIdList = taxonomyResourceV2.getCriterionList()
                .stream()
                // Remove Criteria that are not provided by e-Certis service
                .filter(CriterionFilters::isProvidedByECertis)
                .map(Criterion::getID)
                .collect(Collectors.toList());

        GetFromECertisTask getFromECertisTask;
        URI uri;

        for (String id : initialIdList) {

            uri = new ECertisURIBuilder().buildCriterionURI(id);
            getFromECertisTask = new GetFromECertisTask(uri);

            long startTime = System.currentTimeMillis();
            getFromECertisTask.call();
            long endTime = System.currentTimeMillis();

            System.out.println("Duration for call : " + uri.toString() + " was " + (endTime - startTime));

            Thread.sleep(2000);
        }

    }

}
