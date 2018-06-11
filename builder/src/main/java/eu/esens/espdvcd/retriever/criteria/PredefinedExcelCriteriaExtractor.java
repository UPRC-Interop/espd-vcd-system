package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequestRequirement;
import eu.esens.espdvcd.model.requirement.Requirement;
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
    private final static int nameColumnIndex = 17;
    private final static int descriptionColumnIndex = 18;
    private final static int propertyDataTypeColumnIndex = 21;
    private final static int elementUUIDColumnIndex = 22;
    private final static int elementCodeColumnIndex = 23;

    PredefinedExcelCriteriaExtractor() {
        criterionList = new ArrayList<>();

        try {

            Workbook workbook = new XSSFWorkbook(PredefinedExcelCriteriaExtractor.class.getResourceAsStream(CRITERIA_TAXONOMY_RESOURCE));
            criterionList = new ArrayList<>(65);
            workbook.forEach(sheet -> criterionList.addAll(readDataSheet(sheet)));
            workbook.close();

        } catch (IOException e) {
            Logger.getLogger(PredefinedExcelCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private List<SelectableCriterion> readDataSheet(Sheet dataSheet) {

        // The criteria Column is always the second one
        int criteriaColumn = 1;

        List<SelectableCriterion> crList = new ArrayList<>();
        for (Row r : dataSheet) {

            if ("{CRITERION".equals(getCellStringValueOrNull(r, criteriaColumn))) {
                // We have a criterion in this column.
                SelectableCriterion sc = new SelectableCriterion();
                // These calls must be moved to a row wrapper, for better OO (call should be r.getRowName())
                sc.setName(getRowName(r));
                sc.setDescription(getRowDescription(r));
                sc.setID(getRowUUID(r));
                sc.getRequirementGroups().addAll(extractQuestionGroups(dataSheet, r.getRowNum()+1, criteriaColumn+1));
                crList.add(sc);
            }
        }
        return crList;
    }

    private List<RequirementGroup> extractQuestionGroups(Sheet d, int rowNum, int colNum) {
        List<RequirementGroup> rgList = new ArrayList<>();

        for (int i = rowNum; i < d.getLastRowNum(); i++) {
            if ("{QUESTION_GROUP".equals(getCellStringValueOrNull(d.getRow(rowNum), colNum)) ||
                "{QUESTION_SUBGROUP".equals(getCellStringValueOrNull(d.getRow(rowNum), colNum))) {
                RequirementGroup rg = new RequirementGroup(getRowUUID(d.getRow(rowNum)));
                rg.getRequirementGroups().addAll(extractQuestionGroups(d, i+1, colNum+1));
                rg.getRequirements().addAll(extractQuestions(d, i+1, colNum+1));
                rgList.add(rg);
            }
        }
        return rgList;
    }

    private List<Requirement> extractQuestions(Sheet d, int rowNum, int colNum){

        List<Requirement> rList = new ArrayList<>();
        for (int i = rowNum; i < d.getLastRowNum(); i++) {
            if ("{QUESTION".equals(getCellStringValueOrNull(d.getRow(rowNum), colNum))) {
                Requirement r = new RequestRequirement(
                    getRowUUID(d.getRow(rowNum)),
                    ResponseTypeEnum.valueOf(getRowCode(d.getRow(rowNum))),
                    getRowDescription(d.getRow(rowNum))
                );
                r.setTypeCode(CriterionElementTypeEnum.QUESTION);
                rList.add(r);
            }

        }
        return rList;
    }

    private String getRowUUID(Row r) {
        return getCellStringValueOrNull(r, elementUUIDColumnIndex);
    }

    private String getRowName(Row r) {
        return getCellStringValueOrNull(r, nameColumnIndex);
    }

    private String getRowDescription(Row r) {
        return getCellStringValueOrNull(r, descriptionColumnIndex);
    }

    private String getRowCode(Row r) {
        return getCellStringValueOrNull(r, elementCodeColumnIndex);
    }

    private String getCellStringValueOrNull(Row r, int index) {
        Cell c = r.getCell(index);
        if (c == null) return null;
        return CellType.STRING.equals(c.getCellTypeEnum())?c.getStringCellValue():null;
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
