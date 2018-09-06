package eu.esens.espdvcd.retriever.criteria.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ECertisResourceTest {

    private static final Logger LOGGER = Logger.getLogger(ECertisResourceTest.class.getName());

    private ECertisResource r;

    @Before
    public void setUp() {
        r = new ECertisResource();
        Assert.assertNotNull(r);
    }

    @Test
    public void testECertisCriterionResourceWithInitialIDList() throws Exception {

        CriteriaTaxonomyResource taxonomyResource = new CriteriaTaxonomyResource();

        ECertisResource eCertisResource = new ECertisResource(taxonomyResource
                .getCriterionList()
                .stream().map(sc -> sc.getID()).collect(Collectors.toList()));

        SelectableCriterionPrinter.print(eCertisResource.getCriterionList());
    }

    /**
     * Print all European criteria ID
     *
     * @throws Exception
     */
    @Test
    public void testGetAllCriteriaID() throws Exception {
        r.getAllCriteriaID().forEach(System.out::println);
    }

    /**
     * Print all European criteria ID, Name, Description
     *
     * @throws Exception
     */
    @Test
    public void testGetAllCriteriaBasicInfo() throws Exception {
        r.getAllCriteriaBasicInfo().forEach(SelectableCriterionPrinter::print);
    }

    @Test
    public void testGetFullList() throws Exception {
        SelectableCriterionPrinter.print(r.getCriterionList());
    }

    @Test
    public void testGetEvidencesForCriterion() throws Exception {
        Assert.assertFalse(r.getEvidencesForCriterion("a205fa3b-0719-4c8a-b09d-8f6b2cbf8bd2").isEmpty());
    }

    @Test
    public void testGetECertisCriterion() throws Exception {

        ECertisResource r = new ECertisResource();

        ECertisCriterion ec = r.getECertisCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // Print JSON String
        String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ec);
        System.out.println(prettyCt);
    }

}
