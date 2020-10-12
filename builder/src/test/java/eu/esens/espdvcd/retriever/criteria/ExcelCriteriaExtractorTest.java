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

//import eu.esens.espdvcd.model.SelectableCriterion;
//import eu.esens.espdvcd.model.requirement.RequirementGroup;
//import eu.esens.espdvcd.retriever.exception.RetrieverException;
//import eu.esens.espdvcd.edm.EDMVersion;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//public class ExcelCriteriaExtractorTest {
//
//    private CriteriaExtractor excelCriteriaExtractor;
//    private CriteriaExtractor predefinedCriteriaExtractor;
//
//    @Before
//    public void setUp() {
//        excelCriteriaExtractor = new PredefinedExcelCriteriaExtractor();
//        predefinedCriteriaExtractor = new PredefinedESPDCriteriaExtractor(EDMVersion.V1);
//    }
//
//    @Ignore
//    @Test
//    public void testV1PredefinedAndExcelPredefinedCriterionList() throws RetrieverException {
//
//        final Map<String, SelectableCriterion> fullExcelMap = excelCriteriaExtractor.getFullList()
//                .stream().collect(Collectors.toMap(SelectableCriterion::getID, Function.identity()));
//
//        final Map<String, SelectableCriterion> fullPredefinedV1Map = predefinedCriteriaExtractor.getFullList()
//                .stream().collect(Collectors.toMap(SelectableCriterion::getID, Function.identity()));
//
//        System.out.println("Exist in Criteria Taxonomy V2 -> Does Not Exist in Predefined V1\n");
//        for (String v1Id : fullPredefinedV1Map.keySet()) {
//
//            if (!fullExcelMap.containsKey(v1Id)) {
//                System.out.println("Criterion: " + v1Id + " TypeCode: " + fullPredefinedV1Map.get(v1Id).getTypeCode());
//            }
//        }
//
//        System.out.println("\n\nExist in Predefined V1 -> Does Not Exist in Criteria Taxonomy V2\n");
//        for (String excelId : fullExcelMap.keySet()) {
//
//            if (!fullPredefinedV1Map.containsKey(excelId)) {
//                System.out.println("Criterion: " + excelId + " TypeCode: " + fullExcelMap.get(excelId).getTypeCode());
//            }
//        }
//
//    }
//
//    @Test
//    public void testReadFile() throws RetrieverException {
//
//        excelCriteriaExtractor.getFullList().forEach(sc -> {
//            System.out.println("ID: " + sc.getID() + " Name: " + sc.getName() + " Desc: " + sc.getDescription() + " Element Code: " + sc.getTypeCode());
//            System.out.println("Legislation: " + sc.getLegislationReference());
//            sc.getRequirementGroups().forEach(rqg -> printRequirementGroup(rqg, 3));
//            System.out.println();
//        });
//    }
//
//    private void printRequirementGroup(RequirementGroup rg, int depth) {
//
//        String tabs = "";
//        for (int i = 0; i < depth; i++) {
//            tabs += "\t";
//        }
//        final String finalTabs = tabs;
//        System.out.println(tabs + "RequirementGroup: " + rg.getID() + " Condition: " + rg.getCondition());
//        System.out.println(tabs + "Requirements: ");
//        rg.getRequirements().forEach(r -> {
//            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
//        });
//        final int innerDepth = depth + 1;
//        rg.getRequirementGroups().forEach(rg1 -> printRequirementGroup(rg1, innerDepth));
//    }
//
//}
