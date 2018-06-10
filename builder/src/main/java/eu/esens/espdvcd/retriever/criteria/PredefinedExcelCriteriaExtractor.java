package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PredefinedExcelCriteriaExtractor implements CriteriaExtractor {

    private static final String CRITERIA_TAXONOMY_RESOURCE = "/templates/v2_regulated/ESPD-CriteriaTaxonomy-REGULATED-V2.0.2.xlsx";
    private List<SelectableCriterion> criterionList;
    private static final String REGEX = "\\$";

    PredefinedExcelCriteriaExtractor() {
        criterionList = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(PredefinedExcelCriteriaExtractor.class.getResourceAsStream(CRITERIA_TAXONOMY_RESOURCE));
            workbook.forEach(sheet -> readDataSheet(sheet));
            workbook.close();

        } catch (IOException e) {
            Logger.getLogger(PredefinedExcelCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void readDataSheet(Sheet dataSheet) {

        DataFormatter dataFormatter = new DataFormatter();

        int criterionIndex = 0;
        List<String> rqGroupAsList = null;

        String tempName;
        String tempDescription;
        String tempPropertyDataType;
        String tempElementUUID;
        String tempElementCode;

        final int nameColumnIndex = 17;
        final int descriptionColumnIndex = 18;
        final int propertyDataTypeColumnIndex = 21;
        final int elementUUIDColumnIndex = 22;
        final int elementCodeColumnIndex = 23;

        for (Row row : dataSheet) {

            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);

                if (cellValue.equals("{CRITERION")) {
                    // go and get row specific cell values
                    tempName = dataFormatter.formatCellValue(row.getCell(nameColumnIndex));
                    tempDescription = dataFormatter.formatCellValue(row.getCell(descriptionColumnIndex));
                    tempElementUUID = dataFormatter.formatCellValue(row.getCell(elementUUIDColumnIndex));
                    tempElementCode = dataFormatter.formatCellValue(row.getCell(elementCodeColumnIndex));

                    SelectableCriterion criterion = new SelectableCriterion(tempElementUUID, tempElementCode,
                            tempName, tempDescription, null);
                    criterionList.add(criterionIndex, criterion);
                    break; // go to the next row
                }

                if (cellValue.equals("{LEGISLATION}")) {
                    // no legislation in criteria taxonomy excel
                    break; // go to the next row
                }

                if (cellValue.equals("{QUESTION_GROUP")) {
                    tempElementUUID = dataFormatter.formatCellValue(row.getCell(elementUUIDColumnIndex));
                    tempElementCode = dataFormatter.formatCellValue(row.getCell(elementCodeColumnIndex));

                    rqGroupAsList = new ArrayList<>();
                    rqGroupAsList.add("{QUESTION_GROUP$" + tempElementUUID + "$" + tempElementCode);
                    break; // go to the next row
                }

                if (cellValue.equals("{QUESTION}")) {
                    if (rqGroupAsList != null) {
                        tempDescription = dataFormatter.formatCellValue(row.getCell(descriptionColumnIndex));
                        tempPropertyDataType = dataFormatter.formatCellValue(row.getCell(propertyDataTypeColumnIndex));

                        rqGroupAsList.add("{QUESTION}$" + tempDescription + "$" + tempPropertyDataType);
                    }
                    break; // go to the next row
                }

                if (cellValue.equals("{QUESTION_SUBGROUP")) {
                    if (rqGroupAsList != null) {
                        tempElementUUID = dataFormatter.formatCellValue(row.getCell(elementUUIDColumnIndex));
                        tempElementCode = dataFormatter.formatCellValue(row.getCell(elementCodeColumnIndex));

                        rqGroupAsList.add("{QUESTION_SUBGROUP$" + tempElementUUID + "$" + tempElementCode);
                    }
                    break; // go to the next row
                }

                if (cellValue.equals("QUESTION_SUBGROUP}")) {
                    if (rqGroupAsList != null) {
                        rqGroupAsList.add("QUESTION_SUBGROUP}");
                    }
                    break; // go to the next row
                }

                if (cellValue.equals("QUESTION_GROUP}")) {
                    if (rqGroupAsList != null) {
                        RequirementGroup rqGroup = extractRequirementGroup(rqGroupAsList);
                        criterionList.get(criterionIndex).getRequirementGroups().add(rqGroup);
                    }
                    break; // go to the next row
                }

                if (cellValue.equals("CRITERION}")) {
                    criterionIndex++;
                    break; // go to the next row
                }

            }

        }

    }

    private RequirementGroup extractRequirementGroup(List<String> rqGroupAsList) {
        RequirementGroup rqGroup = null;
        int loopIndex = 0;

        for (String rowValues : rqGroupAsList) {

            String[] values = rowValues.split(REGEX);

            if ("{QUESTION_GROUP".equals(values[0]) || "{QUESTION_SUBGROUP".equals(values[0])) {
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

    @Override
    public List<SelectableCriterion> getFullList() {
        criterionList.forEach(sc -> sc.setSelected(true));
        return criterionList;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return getFullList(initialList, false);
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        Set<SelectableCriterion> fullSet = criterionList.stream()
                .map(sc -> {
                    sc.setSelected(addAsSelected);
                    return sc;
                })
                .collect(Collectors.toSet());
        initialSet.addAll(fullSet);
        return new ArrayList<>(initialSet);
    }

}
