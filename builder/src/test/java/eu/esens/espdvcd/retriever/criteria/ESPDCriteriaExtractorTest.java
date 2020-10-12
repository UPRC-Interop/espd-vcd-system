/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria;

//import eu.esens.espdvcd.builder.model.ModelFactory;
//import eu.esens.espdvcd.builder.edm.SchemaFactory;
//import eu.esens.espdvcd.model.ESPDRequest;
//import eu.esens.espdvcd.model.requirement.RequirementGroup;
//import eu.esens.espdvcd.edm.EDMVersion;
//import eu.espd.edm.v1.espdrequest_1.ESPDRequestType;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.xml.bind.JAXB;
////import grow.names.specification.ubl.edm.xsd.espdrequest_1.ESPDRequestType;
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
