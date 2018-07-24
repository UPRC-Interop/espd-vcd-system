package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.SelectableCriterion;
import org.junit.Before;
import org.junit.Test;

public class ECertisResourceTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testGetAllEUCriteriaWithBasicInfo() throws Exception {
        ECertisResource eCertisResource = new ECertisResource();

        int index = 1;

        for (SelectableCriterion sc : eCertisResource.getAllEUCriteriaWithBasicInfo()) {
            System.out.printf("%-2d %-36s %b\nName: %s\nDescription: %s\n\n", index++, sc.getID(), sc.isSelected(), sc.getName(), sc.getDescription());
        }

    }

}
