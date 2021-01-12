package eu.esens.espdvcd.retriever.criteria.resource.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.retriever.criteria.filters.CriterionFilters;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.utils.ECertisURI;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
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

//        ECertisResource eCertisResource = new ECertisResource();
//        List<String> initialIdList = eCertisResource.getAllCriteriaID();

        Assert.assertFalse(initialIdList.isEmpty());

        GetFromECertisTask getFromECertisTask;
        URI uri;

        for (String id : initialIdList) {

            uri = ECertisURI.baseURL()
                .withCriterionId(id)
                .build().asURI();
            getFromECertisTask = new GetFromECertisTask(uri);

            try {

                //ObjectMapper mapper = new ObjectMapper();
                //Object prettyObj = mapper.readValue(getFromECertisTask.call(), Object.class);
                //String prettyString = mapper.writerWithDefaultPrettyPrinter()
                //    .writeValueAsString(prettyObj);
                //System.out.println(prettyString);

                getFromECertisTask.call();

            } catch (IOException | TimeoutException e) {
                System.err.println(e.getMessage());
            }

        }

    }

    @Ignore
    @Test
    public void testGetMissingCriteria() throws Exception {

        ECertisResource eCertisResource = new ECertisResource();

        List<String> initialIdList = Arrays.asList(
            "61874050-5130-4f1c-a174-720939c7b483",
            "696a75b2-6107-428f-8b74-82affb67e184");

        for (String id : initialIdList) {

            for (ECertisLanguageCodeEnum lang : ECertisLanguageCodeEnum.values()) {

                GetFromECertisTask task = new GetFromECertisTask(
                    ECertisURI.baseURL()
                        .withLang(lang)
                        .withCriterionId(id)
                        .build());

                try {

                    //ObjectMapper mapper = new ObjectMapper();
                    //Object prettyObj = mapper.readValue(task.call(), Object.class);
                    //String prettyString = mapper.writerWithDefaultPrettyPrinter()
                    //    .writeValueAsString(prettyObj);
                    //System.out.println(prettyString);

                    ObjectMapper mapper = new ObjectMapper();
                    Object jsonObject = mapper.readValue(task.call(), Object.class);
                    mapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValue(Paths.get(System.getProperty("user.home") + "/ecertis-copy/criteria/lang/"
                            + lang + "/" + id + ".json").toFile(), jsonObject);

                } catch (IOException | TimeoutException e) {
                    System.err.println(e.getMessage());
                }


                Thread.sleep(200);
            }

        }

    }

}
