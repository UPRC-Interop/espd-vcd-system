package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ExcelCriteriaExtractorTest {

    private static final String REGEX = "\\$";

    @Before
    public void setUp() {

    }

    @Ignore
    @Test
    public void testReadFile() throws RetrieverException {
        CriteriaExtractor extractor = new PredefinedExcelCriteriaExtractor();
        // CriteriaExtractor extractor = new PredefinedESPDCriteriaExtractor(SchemaVersion.V1);

        extractor.getFullList().forEach(sc -> {
            System.out.println("ID: " + sc.getID() + " Name: " + sc.getName() + " Desc: " + sc.getDescription() + " Element Code: " + sc.getTypeCode());
            System.out.println("Legislation: " + sc.getLegislationReference());
            sc.getRequirementGroups().forEach(rqg -> printRequirementGroup(rqg, 2));
            System.out.println();
        });
    }

    @Ignore
    @Test
    public void testRGRead() {
        String[] rqGroupAsList = {"{QUESTION_GROUP$7c637c0c-7703-4389-ba52-02997a055bd7$ON*",
                "{QUESTION}$Your answer$INDICATOR",
                "{QUESTION_SUBGROUP$f5276600-a2b6-4ff6-a90e-b31fe19dae41$ONTRUE",
                "{QUESTION}$Date of conviction$DATE",
                "{QUESTION}$Reason$DESCRIPTION",
                "{QUESTION}$Who has been convicted$DESCRIPTION",
                "{QUESTION}$Length of the period of exclusion$DESCRIPTION",
                "{QUESTION_SUBGROUP$f4978772-3126-4ded-bc30-f50da8c3a038$ONTRUE",
                "{QUESTION}$Have you taken measures to demonstrate your reliability (Self-Cleaning)?$INDICATOR",
                "{QUESTION_SUBGROUP$74e6c7b4-757b-4b40-ada6-fad6a997c310$ONTRUE",
                "{QUESTION}$Please describe them$DESCRIPTION",
                "QUESTION_SUBGROUP}",
                "QUESTION_SUBGROUP}",
                "QUESTION_SUBGROUP}",
                "QUESTION_GROUP}"};

        extractRequirementGroup(Arrays.asList(rqGroupAsList));
    }

    private RequirementGroup extractRequirementGroup(List<String> rqGroupAsList) {
        RequirementGroup rqGroup = null;
        int loopIndex = 0;

        for (String rowValues : rqGroupAsList) {

            String[] values = rowValues.split(REGEX);

            if ("{QUESTION_GROUP".equals(values[0])) {
                rqGroup = new RequirementGroup(values[1]); // UUID
                rqGroup.setCondition(values[2]);           // PropertyGroupTypeCode
                loopIndex++;
                continue;
            }

            if ("{QUESTION}".equals(values[0]) && rqGroup != null) {

                rqGroup.getRequirements().add(new ResponseRequirement(UUID.randomUUID().toString(),
                        ResponseTypeEnum.valueOf(values[2]),
                        values[1])); // description
                loopIndex++;
                continue;
            }

            if ("{QUESTION_SUBGROUP".equals(values[0]) && rqGroup != null) {
                List<String> rqSubGroupAsList = new ArrayList<>();
                int innerIndex = loopIndex;

                while (!"QUESTION_SUBGROUP}".equals(rqGroupAsList.get(innerIndex).split("$")[0])) {
                    rqSubGroupAsList.add(rqGroupAsList.get(innerIndex));
                    innerIndex++;
                }

                rqGroup.getRequirementGroups().add(extractRequirementGroup(rqSubGroupAsList));
            }

            loopIndex++;
        }

        return rqGroup;
    }

    private void printRequirementGroup(RequirementGroup rg, int depth) {

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs + "RequirementGroup: " + rg.getID() + " Condition: " + rg.getCondition());
        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> printRequirementGroup(rg1, innerDepth));
    }

}
