package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.xml.bind.JAXB;
import java.util.ArrayList;
import java.util.List;

public class ESPDTenderingCriteriaExtractorTest {

    static int maxDepth = 1;

    public ESPDTenderingCriteriaExtractorTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of getFullList method, of class {@link PredefinedESPDTenderingCriteriaExtractor}
     */

    @Test
    public void testGetFullList() {
        PredefinedESPDTenderingCriteriaExtractor ce = new PredefinedESPDTenderingCriteriaExtractor(QualificationApplicationTypeEnum.REGULATED);
        ce.getFullList().stream()
                .forEach(c -> {
                    System.out.println(c.getID() + " " + c.getName() + " (" + c.getTypeCode() + ")");
                    c.getRequirementGroups().forEach(rg -> traverseRequirementGroup(rg, 1));
                });
        System.out.println("Max Depth: " + maxDepth);
    }

    private void traverseRequirementGroup(RequirementGroup rg, int depth) {

        if (depth > maxDepth) {
            maxDepth = depth;
        }

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs + "RequirementGroup: " + rg.getID());
        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> traverseRequirementGroup(rg1, innerDepth));
    }

    @Test
    public void loadTransformAndDisplayTest() {

        QualificationApplicationRequestType reqType =
                JAXB.unmarshal(ESPDTenderingCriteriaExtractorTest.class
                                .getResourceAsStream("/espd-request.xml"),
                        QualificationApplicationRequestType.class);

        ESPDRequest req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);

        QualificationApplicationRequestType req2Type = SchemaFactory.V2.ESPD_REQUEST.extractQualificationApplicationRequestType(req);
        JAXB.marshal(req2Type, System.out);

    }

    @Ignore
    @Test
    public void comparePredefinedCriteriaExtractors() throws RetrieverException {
        findAndPrintV1WhichExistInV2();
        findAndPrintV2WhichExistInV1();
    }

    private void findAndPrintV1WhichExistInV2() throws RetrieverException {
        CriteriaExtractor predefinedCriteriaExtractor = new PredefinedESPDCriteriaExtractor();
        CriteriaExtractor predefinedTenderingCriteriaExtractor = new PredefinedESPDTenderingCriteriaExtractor(QualificationApplicationTypeEnum.REGULATED);

        List<SelectableCriterion> predefinedCriteriaList = predefinedCriteriaExtractor.getFullList();
        List<SelectableCriterion> predefinedTenderingCriteriaList = predefinedTenderingCriteriaExtractor.getFullList();

        List<SelectableCriterion> criteriaV1WhichExistInV2List = new ArrayList<>();
        List<SelectableCriterion> criteriaV1WhichDoesNotExistInV2List = new ArrayList<>();

        for (SelectableCriterion pc : predefinedCriteriaList) {
            boolean isFound = false;

            for (SelectableCriterion ptc : predefinedTenderingCriteriaList) {

                if (pc.getID().equals(ptc.getID())) {
                    criteriaV1WhichExistInV2List.add(pc);
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                criteriaV1WhichDoesNotExistInV2List.add(pc);
            }
        }

        System.out.println("Criteria from v1 which exist in v2" + " (" + criteriaV1WhichExistInV2List.size() + " criteria)");
        System.out.println("----------------------------------");
        criteriaV1WhichExistInV2List.forEach(pc -> System.out.println(pc.getID() + " " + pc.getTypeCode()));

        System.out.println("Criteria from v1 which does not exist in v2" + " (" + criteriaV1WhichDoesNotExistInV2List.size() + " criteria)");
        System.out.println("-------------------------------------------");
        criteriaV1WhichDoesNotExistInV2List.forEach(ptc -> System.out.println(ptc.getID() + " " + ptc.getTypeCode()));
    }

    private void findAndPrintV2WhichExistInV1() throws RetrieverException {
        CriteriaExtractor predefinedCriteriaExtractor = new PredefinedESPDCriteriaExtractor();
        CriteriaExtractor predefinedTenderingCriteriaExtractor = new PredefinedESPDTenderingCriteriaExtractor(QualificationApplicationTypeEnum.REGULATED);

        List<SelectableCriterion> predefinedCriteriaList = predefinedCriteriaExtractor.getFullList();
        List<SelectableCriterion> predefinedTenderingCriteriaList = predefinedTenderingCriteriaExtractor.getFullList();

        List<SelectableCriterion> criteriaV2WhichExistInV1List = new ArrayList<>();
        List<SelectableCriterion> criteriaV2WhichDoesNotExistInV1List = new ArrayList<>();

        for (SelectableCriterion ptc : predefinedTenderingCriteriaList) {
            boolean isFound = false;

            for (SelectableCriterion pc : predefinedCriteriaList) {

                if (ptc.getID().equals(pc.getID())) {
                    criteriaV2WhichExistInV1List.add(ptc);
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                criteriaV2WhichDoesNotExistInV1List.add(ptc);
            }
        }

        System.out.println("Criteria from v2 which exist in v1" + " (" + criteriaV2WhichExistInV1List.size() + " criteria)");
        System.out.println("----------------------------------");
        criteriaV2WhichExistInV1List.forEach(pc -> System.out.println(pc.getID() + " " + pc.getTypeCode()));

        System.out.println("Criteria from v2 which does not exist in v1" + " (" + criteriaV2WhichDoesNotExistInV1List.size() + " criteria)");
        System.out.println("-------------------------------------------");
        criteriaV2WhichDoesNotExistInV1List.forEach(ptc -> System.out.println(ptc.getID() + " " + ptc.getTypeCode()));
    }
}
