package eu.esens.espdvcd.retriever.criteria;

//import eu.esens.espdvcd.builder.model.ModelFactory;
//import eu.esens.espdvcd.builder.schema.SchemaFactory;
//import eu.esens.espdvcd.model.ESPDRequest;
//import eu.esens.espdvcd.model.requirement.RequirementGroup;
//import eu.esens.espdvcd.schema.EDMVersion;
//import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.xml.bind.JAXB;
////import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
//
//public class ESPDCriteriaExtractorTest {
//
//    static int maxDepth = 1;
//    public ESPDCriteriaExtractorTest() {
//    }
//
//    @Before
//    public void setUp() {
//    }
//
//    /**
//     * Test of getFullList method, of class PredefinedESPDCriteriaExtractor.
//     */
//
//    @Test
//    public void testGetFullList() {
//        PredefinedESPDCriteriaExtractor ce = new PredefinedESPDCriteriaExtractor(EDMVersion.V1);
//        ce.getFullList().stream()
//                .forEach(c -> {
//                    System.out.println(c.getID() + " " + c.getName() + " (" + c.getTypeCode() + ")");
//                    c.getRequirementGroups().forEach(rg -> traverseRequirementGroup(rg,1));
//                });
//        System.out.println("Max Depth: "+maxDepth);
//    }
//
//    private void traverseRequirementGroup(RequirementGroup rg, int depth) {
//
//        if (depth > maxDepth) {
//           maxDepth = depth;
//        }
//
//        String tabs = "";
//        for (int i=0; i<depth; i++) {
//            tabs +="\t";
//        }
//        final String finalTabs = tabs;
//        System.out.println(tabs+"RequirementGroup: " + rg.getID());
//        System.out.println(tabs+"Requirements: ");
//        rg.getRequirements().forEach(r -> {
//            System.out.println(finalTabs+"\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
//        });
//        final int innerDepth = depth+1;
//        rg.getRequirementGroups().forEach(rg1 -> traverseRequirementGroup(rg1, innerDepth));
//    }
//
//    @Test
//    public void loadTransformAndDisplayTest() {
//
//        ESPDRequestType reqType = JAXB.unmarshal(ESPDCriteriaExtractorTest.class.getResourceAsStream("/espd-request.xml"), ESPDRequestType.class);
//
//        // ESPDRequest req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
//        ESPDRequest req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
//
//        ESPDRequestType req2Type = SchemaFactory.withSchemaVersion1().ESPD_REQUEST.extractESPDRequestType(req);
//        JAXB.marshal(req2Type, System.out);
//
//    }
//}
