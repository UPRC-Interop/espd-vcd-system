/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.RequirementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.retriever.criteria.resource.enums.CardinalityEnum;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;
import eu.esens.espdvcd.retriever.criteria.resource.utils.CardinalityUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaTaxonomyResource implements CriteriaResource, RequirementsResource {

    private static final Logger LOGGER = Logger.getLogger(CriteriaTaxonomyResource.class.getName());

    private static final String CRITERIA_TAXONOMY_RESOURCE = "/templates/v2_regulated/ESPD-CriteriaTaxonomy-REGULATED-V2.0.2.xlsx";
    // private static final String CRITERIA_TAXONOMY_RESOURCE = "/templates/v2_regulated/ESPD-CriteriaTaxonomy-REGULATED-V2.0.2-FIXED.xlsx";
    private final static int nameColumnIndex = 17;
    private final static int descriptionColumnIndex = 18;
    private final static int cardinalityColumnIndex = 20;
    private final static int propertyDataTypeColumnIndex = 21;
    private final static int elementUUIDColumnIndex = 22;
    private final static int elementCodeColumnIndex = 23;

    private List<SelectableCriterion> criterionList;
    private Map<String, List<RequirementGroup>> rgMap;

    public CriteriaTaxonomyResource() {
        rgMap = new HashMap<>();

        try {

            Workbook workbook = new XSSFWorkbook(CriteriaTaxonomyResource.class.getResourceAsStream(CRITERIA_TAXONOMY_RESOURCE));
            criterionList = new ArrayList<>(65);
            workbook.forEach(sheet -> criterionList.addAll(readDataSheet(sheet)));
            workbook.close();

            criterionList.forEach(sc -> rgMap.put(sc.getID(), sc.getRequirementGroups()));

        } catch (IOException e) {
            Logger.getLogger(CriteriaTaxonomyResource.class.getName()).log(Level.SEVERE, null, e);
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
                        sc.setTypeCode(getRowCode(r));
                        sc.setSelected(true);
                        sc.getRequirementGroups().addAll(
                                extractQuestionGroups(dataSheet, r.getRowNum() + 1,
                                        r2.getRowNum() + 1, criteriaColumn + 1));
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
                        RequirementGroup rg = new RequirementGroup(getRowUUID(d.getRow(sRow)));
                        rg.setCondition(getRowCode(d.getRow(sRow)));
                        // setting cardinality here
                        CardinalityEnum cardinality = CardinalityUtils.extractCardinality(getRowCardinality(d.getRow(sRow)));
                        rg.setMandatory(cardinality.isMandatory());
                        rg.setMultiple(cardinality.isMultiple());
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
                Requirement r = new ResponseRequirement(
                        UUID.randomUUID().toString(),
                        ResponseTypeEnum.valueOf(getRowResponseType(d.getRow(i))),
                        getRowDescription(d.getRow(i)) //+ "(at " + i + "," + colNum + ")"
                );
                r.setType(RequirementTypeEnum.QUESTION);
                // setting cardinality here
                CardinalityEnum cardinality = CardinalityUtils.extractCardinality(getRowCardinality(d.getRow(i)));
                r.setMandatory(cardinality.isMandatory());
                r.setMultiple(cardinality.isMultiple());
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

    private String getRowResponseType(Row r) {
        return getCellStringValueOrNull(r, propertyDataTypeColumnIndex);
    }

    private String getRowCardinality(Row r) {
        return getCellStringOrNumericValueOrNull(r, cardinalityColumnIndex);
    }

    private String getCellStringValueOrNull(Row r, int index) {
        Cell c = r.getCell(index);
        if (c == null) {
            return null;
        }
        return CellType.STRING.equals(c.getCellTypeEnum()) ? c.getStringCellValue() : null;
    }

    private String getCellStringOrNumericValueOrNull(Row r, int index) {
        Cell c = r.getCell(index);
        if (c == null) {
            return null;
        }

        if (CellType.STRING.equals(c.getCellTypeEnum())) {
            return c.getStringCellValue();
        }

        if (CellType.NUMERIC.equals(c.getCellTypeEnum())) {
            return String.valueOf((int) c.getNumericCellValue());
        }

        return null;
    }

    public void applyCardinalities(SelectableCriterion sc) {
        // find root RequirementGroup/s of that criterion from taxonomy
        final List<RequirementGroup> rgListFromTaxonomy = rgMap.get(sc.getID());
        // apply cardinalities to all root RequirementGroup/s
        sc.getRequirementGroups().forEach(rg -> applyCardinalities(
                rgListFromTaxonomy.stream()
                        .filter(rgFromTaxonomy -> rg.getID().equals(rgFromTaxonomy.getID()))
                        .findFirst().orElse(null) // from
                , rg));                                 //  to
    }

    public void applyCardinalities(RequirementGroup from, RequirementGroup to) {

        if (from != null && to != null) {

            // do the same for sub-RequirementGroup/s
            to.getRequirementGroups().forEach(rg -> applyCardinalities(
                    from.getRequirementGroups().stream()
                            .filter(rgFromTaxonomy -> rg.getID().equals(rgFromTaxonomy.getID()))
                            .findFirst().orElse(null) // from
                    , rg));                                 //  to

            // do the same for requirements
            // to.getRequirements().forEach(rq -> from.getRequirements().forEach());

            to.setMultiple(from.isMultiple());
            to.setMandatory(from.isMandatory());
        }
    }

    @Override
    public List<SelectableCriterion> getCriterionList() {
        return getCriterionList(EULanguageCodeEnum.EN);
    }

    @Override
    public List<SelectableCriterion> getCriterionList(EULanguageCodeEnum lang) {

        // fallback
        if (lang == null || lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return criterionList;
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() {
        return getCriterionMap(EULanguageCodeEnum.EN);
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap(EULanguageCodeEnum lang) {

        // fallback
        if (lang == null || lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return criterionList.stream().collect(Collectors.toMap(sc -> sc.getID(), Function.identity()));
    }

    @Override
    public List<RequirementGroup> getRequirementsForCriterion(String ID) {
        return getRequirementsForCriterion(ID, EULanguageCodeEnum.EN);
    }

    @Override
    public List<RequirementGroup> getRequirementsForCriterion(String ID, EULanguageCodeEnum lang) {

        // fallback
        if (lang == null || lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        return rgMap.get(ID);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.TAXONOMY;
    }

}
