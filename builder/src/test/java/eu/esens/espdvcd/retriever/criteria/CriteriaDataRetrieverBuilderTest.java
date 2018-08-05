package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author konstantinos
 */
public class CriteriaDataRetrieverBuilderTest {


    private CriteriaDataRetriever retriever;

    @Before
    public void setUp() {
        retriever = new CriteriaDataRetrieverBuilder().build();
    }

    @Test
    public void testGetCriterion() throws Exception {
//            extractor.setLang("el");
//
//            ECertisCriterion theCriterion = extractor.getCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");

//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // Print JSON String
//            String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(theCriterion);
//            System.out.println(prettyCt);

        SelectableCriterion sc = retriever.getCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");
        SelectableCriterionPrinter.print(sc);
    }

//    @Test
//    public void testEuToNationalMapping() throws RetrieverException {
//
//        String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
//        String code = "gr";
//
//        List<String> results = extractor.getNationalCriterionMapping(ID, code)
//                .stream()
//                .map(c -> c.getID()).collect(Collectors.toList());
//
//        System.out.println("- EU to National (" + code + ") Criterion Mapping");
//        System.out.printf("%s --> %s%n", ID, results);
//    }
//
//    @Test
//    public void testNationalToNationalMapping() throws RetrieverException {
//
//        String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
//        String code = "be";
//
//        List<String> results = extractor.getNationalCriterionMapping(ID, code)
//                .stream()
//                .map(c -> c.getID()).collect(Collectors.toList());
//
//        System.out.println("- Natinal to National (" + code + ") Criterion Mapping");
//        System.out.printf("%s --> %s%n", ID, results);
//    }
//
//    @Test
//    public void testGetCriterion() throws RetrieverException {
//
//        ECertisCriterion c = extractor.getCriterion("3f865345-9a7e-49a3-924a-ca77da6f2512");
//
//        assertEquals("3f865345-9a7e-49a3-924a-ca77da6f2512", c.getID());
//    }
//
//    @Test
//    public void testGetEvidenceGroup() throws RetrieverException {
//
//        final String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
//
//        extractor.getEvidences(ID).forEach(eg -> traverseEvidenceGroup(eg));
//    }
//
//    private void traverseEvidenceGroup(ECertisEvidenceGroup eg) {
//        System.out.println("Traverse Evidence Group");
//        System.out.println("\tID: " + eg.getID());
//        eg.getEvidences()
//                .stream()
//                .forEach(ee -> {
//                    System.out.println("\t\tID: " + ee.getID());
//                    System.out.println("\t\tTypeCode: " + ee.getTypeCode());
//                });
//    }


}
