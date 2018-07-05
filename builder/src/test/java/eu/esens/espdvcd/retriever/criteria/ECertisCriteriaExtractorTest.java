package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author konstantinos
 */
public class ECertisCriteriaExtractorTest {

    private ECertisCriteriaExtractor extractor;

    @Before
    public void setUp() {
        extractor = new ECertisCriteriaExtractor(SchemaVersion.V2);
    }

    @Test
    public void testGetAllEUCriteriaIDs() throws RetrieverException {

        int index = 1;

        for (String id : extractor.getAllEuropeanCriteriaID()) {
            System.out.println("(" + index + ") " + id);
            index++;
        }

    }


    @Ignore
    @Test
    public void testPredefinedAndECertisCriteriaID() throws RetrieverException {

        PredefinedESPDCriteriaExtractor predefinedESPDCriteriaExtractor =
                new PredefinedESPDCriteriaExtractor(SchemaVersion.V2);

        final List<String> ecertisIDList = extractor.getAllEuropeanCriteriaID();
        final List<String> predefinedIDList = predefinedESPDCriteriaExtractor.getFullList().stream()
                .map(sc -> sc.getID())
                .collect(Collectors.toList());

        final Map<String, SelectableCriterion> eCertisCriterionMap = new HashMap<>();
        extractor.getFullList().forEach(sc -> eCertisCriterionMap.put(sc.getID(), sc));

        System.out.println("Number of e-Certis Criteria: " + ecertisIDList.size());
        System.out.println("Number of Predefined Criteria: " + predefinedIDList.size());

        if (ecertisIDList.containsAll(predefinedIDList)
                && ecertisIDList.size() == predefinedIDList.size()) {
            System.out.println("Predefined ID list contains exactly the same UUIDs with e-Certis ID list");
        } else if (ecertisIDList.containsAll(predefinedIDList)) {
            System.out.println("Predefined ID list is a subset of e-Certis ID list");
        } else if (predefinedIDList.containsAll(ecertisIDList)) {
            System.out.println("e-Certis ID list is a subset of Predefined ID list");
        } else {

            System.out.println("Predefined IDs that are not contained in e-Certis list:");
            int index1 = 1;
            for (String id : predefinedIDList) {
                if (!ecertisIDList.contains(id)) {
                    System.out.println("(" + index1 + ") " + id + " " + (eCertisCriterionMap.get(id) != null ? eCertisCriterionMap.get(id).getTypeCode() : null));
                    index1++;
                }
            }

            System.out.println("\ne-Certis IDs that are not contained in Predefined list:");
            int index2 = 1;
            for (String id : ecertisIDList) {
                if (!predefinedIDList.contains(id)) {
                    System.out.println("(" + index2 + ") " + id + " " + (eCertisCriterionMap.get(id) != null ? eCertisCriterionMap.get(id).getTypeCode() : null));
                    index2++;
                }
            }

        }

    }

    // @Ignore
    @Test
    public void testGetFullListTwice() throws RetrieverException {

        long start1 = System.currentTimeMillis();
        testGetFullList();
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        testGetFullList();
        long end2 = System.currentTimeMillis();

        System.out.println("First time: " + (end1 - start1) + " msecs");
        System.out.println("Second time: " + (end2 - start2) + " msecs");
    }

    @Test
    public void testGetFullList() throws RetrieverException {

        List<SelectableCriterion> criteriaList = extractor.getFullList();
        // List<SelectableCriterion> criteriaList = CriteriaExtractorFactory.getPredefinedESPDCriteriaExtractor(SchemaVersion.V2).getFullList();
        int numberOfCriterion = 1;

        for (SelectableCriterion sc : criteriaList) {
            System.out.println("(" + numberOfCriterion + ") " + sc.getID() + " " + sc.getName() + " (" + sc.getTypeCode() + ")");
            Optional.ofNullable(sc.getLegislationReference())
                    .ifPresent(lr -> traverseLegislationReference(lr));
            numberOfCriterion++;
        }

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
