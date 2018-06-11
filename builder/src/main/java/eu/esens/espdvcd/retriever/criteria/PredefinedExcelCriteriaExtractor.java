package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.CriterionElementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequestRequirement;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PredefinedExcelCriteriaExtractor implements CriteriaExtractor {

  private static final String CRITERIA_TAXONOMY_RESOURCE = "/templates/v2_regulated/ESPD-CriteriaTaxonomy-REGULATED-V2.0.2.xlsx";
  private List<SelectableCriterion> criterionList;
  private final static int nameColumnIndex = 17;
  private final static int descriptionColumnIndex = 18;
  private final static int propertyDataTypeColumnIndex = 21;
  private final static int elementUUIDColumnIndex = 22;
  private final static int elementCodeColumnIndex = 23;

  Logger logger = Logger.getLogger(PredefinedExcelCriteriaExtractor.class.getCanonicalName());

  PredefinedExcelCriteriaExtractor() {
    criterionList = new ArrayList<>();

    try {

      Workbook workbook = new XSSFWorkbook(
          PredefinedExcelCriteriaExtractor.class.getResourceAsStream(CRITERIA_TAXONOMY_RESOURCE));
      criterionList = new ArrayList<>(65);
      Sheet first = workbook.getSheetAt(0);
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
    Iterator<Row> it = dataSheet.iterator();
    while (it.hasNext()) {
      Row r = it.next();
      if ("{CRITERION".equals(getCellStringValueOrNull(r, criteriaColumn))) {
        // we found row start. Now we need row end
        boolean foundEnd = false;
        while (!foundEnd) {
          Row r2 = it.next();
          if ("CRITERION}".equals(getCellStringValueOrNull(r2, criteriaColumn))) {
            // found end invoke calls;
            foundEnd = true;
            SelectableCriterion sc = new SelectableCriterion();
            sc.setName(getRowName(r));
            sc.setDescription(getRowDescription(r));
            sc.setID(getRowUUID(r));
            sc.getRequirementGroups().addAll(
                extractQuestionGroups(dataSheet, r.getRowNum() + 1, r2.getRowNum() + 1,
                    criteriaColumn + 1));
            crList.add(sc);
          }
        }
      }
    }
    return crList;
  }

  private List<RequirementGroup> extractQuestionGroups(Sheet d, int startRowNum, int endRowNum,
      int colNum) {
    List<RequirementGroup> rgList = new ArrayList<>();

    for (int i = startRowNum; i < endRowNum; i++) {

      if ("{QUESTION_GROUP".equals(getCellStringValueOrNull(d.getRow(i), colNum)) ||
          "{QUESTION_SUBGROUP".equals(getCellStringValueOrNull(d.getRow(i), colNum))) {
        int sRow = i;
        for (int j = i + 1; j < endRowNum; j++) {

          if ("QUESTION_GROUP}".equals(getCellStringValueOrNull(d.getRow(j), colNum)) ||
              "QUESTION_SUBGROUP}".equals(getCellStringValueOrNull(d.getRow(j), colNum))) {
            i = j;
            RequirementGroup rg = new RequirementGroup(getRowUUID(d.getRow(j)));
            rg.getRequirementGroups().addAll(extractQuestionGroups(d, sRow, (j), colNum + 1));
            rg.getRequirements().addAll(extractQuestions(d, sRow, j, colNum + 1));
            rgList.add(rg);
            break;
          }
        }
      }
    }
    return rgList;
  }

  private List<Requirement> extractQuestions(Sheet d, int rowNum, int endRowNum, int colNum) {

    List<Requirement> rList = new ArrayList<>();
    for (int i = rowNum; i < endRowNum; i++) {
      if ("{QUESTION}".equals(getCellStringValueOrNull(d.getRow(i), colNum))) {
        Requirement r = new RequestRequirement(
            getRowUUID(d.getRow(i)),
            ResponseTypeEnum.valueOf(getResponseType(d.getRow(i))),
            getRowDescription(d.getRow(i)) //+ "(at " + i + "," + colNum + ")"
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

  private String getResponseType(Row r) {
    return getCellStringValueOrNull(r, propertyDataTypeColumnIndex);
  }

  private String getCellStringValueOrNull(Row r, int index) {
    Cell c = r.getCell(index);
    if (c == null) {
      return null;
    }
    return CellType.STRING.equals(c.getCellTypeEnum()) ? c.getStringCellValue() : null;
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
  public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList,
      boolean addAsSelected) {
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
