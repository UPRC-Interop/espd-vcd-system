package eu.esens.espdvcd.retriever.criteria.resource;

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

}
