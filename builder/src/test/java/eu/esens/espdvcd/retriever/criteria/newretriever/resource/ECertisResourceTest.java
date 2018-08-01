package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ECertisResourceTest {

    private static final Logger LOGGER = Logger.getLogger(ECertisResourceTest.class.getName());

    @Before
    public void setUp() {

    }

    @Test
    public void testGetAllEUCriteriaWithBasicInfo() throws Exception {
        ECertisResource resource = new ECertisResource();

        int index = 1;

        for (SelectableCriterion sc : resource.getAllEUCriteriaWithBasicInfo()) {
            System.out.printf("%-2d %-36s %b\nName: %s\nDescription: %s\n\n", index++, sc.getID(), sc.isSelected(), sc.getName(), sc.getDescription());
        }

    }

    @Test
    public void testCreateECertisCriterionMap() {
        ECertisResource resource = new ECertisResource();
        boolean exceptionHappened = false;
        int numberOfLoops = 1;

        for (int i = 0; i < numberOfLoops; i++) {
            exceptionHappened = performECertisCriterionMapCreation(resource);
        }

        Assert.assertFalse(exceptionHappened);
    }

    private boolean performECertisCriterionMapCreation(ECertisResource resource) {

        int index = 1;
        boolean exceptionHappened = false;

        try {

            for (ECertisCriterion ec : resource.createECertisCriterionMap().values()) {
                System.out.printf("%-2d %-36s\nName: %s\nDescription: %s\n\n", index++, ec.getID(), ec.getName(), ec.getDescription());
            }

        } catch (RetrieverException e) {
            exceptionHappened = true;
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return exceptionHappened;
    }

}
