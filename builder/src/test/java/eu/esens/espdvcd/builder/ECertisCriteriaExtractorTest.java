package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractorTest {

    private ECertisCriteriaExtractor extractor;
    
    @Before
    public void setUp() {
        extractor = new ECertisCriteriaExtractor();
    }

    @Test
    public void testGetFullList() throws RetrieverException {
          
        List<SelectableCriterion> criteriaList = extractor.getFullList();
        
        criteriaList.forEach((SelectableCriterion sc) -> {
            System.out.println(sc.getID() + " " + sc.getName() + " (" + sc.getTypeCode() + ")");
            traverseLegislationReference(sc.getLegislationReference());
        });
        
        System.out.printf("Number of EU Criteria: %d%n", criteriaList.size());
    }

    private void traverseLegislationReference(LegislationReference lr) {

        String tab = "\t";

        System.out.println("\n" + tab + "LegislationReference");
        System.out.println(tab + "====================");
        System.out.println(tab + "Title: " + lr.getTitle());
        System.out.println(tab + "Description: " + lr.getDescription());
        System.out.println(tab + "JurisdictionLevelCode: " + lr.getJurisdictionLevelCode());
        System.out.println(tab + "Article: " + lr.getArticle());
        System.out.println(tab + "URI: " + lr.getURI() + "\n");
    }

}
