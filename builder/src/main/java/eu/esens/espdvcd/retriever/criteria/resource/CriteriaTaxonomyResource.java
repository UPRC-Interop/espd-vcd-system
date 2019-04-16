/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.RequirementGroupTypeEnum;
import eu.esens.espdvcd.codelist.enums.RequirementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.retriever.criteria.resource.enums.CardinalityEnum;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;
import eu.esens.espdvcd.retriever.criteria.resource.utils.TaxonomyDataUtils;
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
public abstract class CriteriaTaxonomyResource implements CriteriaResource, RequirementsResource {

    private static final Logger LOGGER = Logger.getLogger(CriteriaTaxonomyResource.class.getName());

    private final static int nameColumnIndex = 17;
    private final static int descriptionColumnIndex = 18;
    private final static int cardinalityColumnIndex = 20;
    private final static int propertyDataTypeColumnIndex = 21;
    private final static int elementUUIDColumnIndex = 22;
    private final static int elementCodeColumnIndex = 23;
    private final static int codelistColumnIndex = 24;
    private final static int propertyKey1ColumnIndex = 26;
    private final static int propertyKey2ColumnIndex = 27;
    private final static int propertyKey3ColumnIndex = 28;

    protected List<SelectableCriterion> criterionList;
    protected Map<String, SelectableCriterion> criterionMap;
    protected Map<String, List<RequirementGroup>> rgMap;

    public CriteriaTaxonomyResource(String path) {
        rgMap = new HashMap<>();

        try {

            Workbook workbook = new XSSFWorkbook(CriteriaTaxonomyResource.class.getResourceAsStream(path));
            criterionList = new ArrayList<>(65);
            criterionMap = new HashMap<>(65);
            workbook.forEach(sheet -> criterionList.addAll(readDataSheet(sheet)));
            workbook.close();

            criterionList.forEach(sc -> rgMap.put(sc.getID(), sc.getRequirementGroups()));
            criterionList.forEach(sc -> criterionMap.put(sc.getID(), sc));

        } catch (IOException e) {
            Logger.getLogger(CriteriaTaxonomyResource.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Apply cardinalities and  type
     *
     * @param sc
     */
    public void applyTaxonomyData(SelectableCriterion sc) {
        // find root RequirementGroup/s of that criterion from taxonomy
        final SelectableCriterion from = criterionMap.get(sc.getID());

        if (from == null) {
            LOGGER.log(Level.SEVERE, "SC with ID " + sc.getID() + " cannot be found in rgMap");
            return;
        }

        // apply taxonomy data to sc
        TaxonomyDataUtils.applyTaxonomyData(from, sc);
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
                        sc.getPropertyKeyMap().put("pk1", getRowPropertyKey(r, 1)); // property key for Name
                        sc.getPropertyKeyMap().put("pk2", getRowPropertyKey(r, 2)); // property key for Description

                        sc.getRequirementGroups().addAll(
                                extractAllRequirementGroupType(dataSheet, r.getRowNum() + 1,
                                        r2.getRowNum() + 1, criteriaColumn + 1));
                        crList.add(sc);
                    }
                }
            }
        }
        return crList;
    }

    private List<RequirementGroup> extractAllRequirementGroupType(Sheet d, int startRowNum, int endRowNum, int colNum) {
        List<RequirementGroup> rgList = new ArrayList<>();

        for (int i = startRowNum; i < endRowNum; i++) {

            if ("{QUESTION_GROUP".equals(getCellStringValueOrNull(d.getRow(i), colNum)) ||
                    "{QUESTION_SUBGROUP".equals(getCellStringValueOrNull(d.getRow(i), colNum)) ||
                    "{REQUIREMENT_GROUP".equals(getCellStringValueOrNull(d.getRow(i), colNum)) ||
                    "{REQUIREMENT_SUBGROUP".equals(getCellStringValueOrNull(d.getRow(i), colNum))) {

                int sRow = i;
                for (int j = i + 1; j < endRowNum; j++) {

                    String cellValue = getCellStringValueOrNull(d.getRow(j), colNum);

                    if ("QUESTION_GROUP}".equals(cellValue) ||
                            "QUESTION_SUBGROUP}".equals(cellValue) ||
                            "REQUIREMENT_GROUP}".equals(cellValue) ||
                            "REQUIREMENT_SUBGROUP}".equals(cellValue)) {

                        i = j;
                        RequirementGroup rg = new RequirementGroup(getRowUUID(d.getRow(sRow)));
                        rg.setCondition(getRowCode(d.getRow(sRow)));
                        rg.setType(extractRequirementGroupType(cellValue));
                        // setting cardinality here
                        CardinalityEnum c = TaxonomyDataUtils.extractCardinality(getRowCardinality(d.getRow(sRow)));
                        applyCardinality(rg, c);
                        rg.getRequirementGroups().addAll(extractAllRequirementGroupType(d, sRow, (j), colNum + 1));
                        rg.getRequirements().addAll(extractAllRequirementType(d, sRow, j, colNum + 1));
                        rgList.add(rg);
                        break;
                    }
                }
            }
        }
        return rgList;
    }

    private Requirement createResponseRequirementOfType(ResponseTypeEnum responseType, String desc, RequirementTypeEnum type) {
        Requirement r = new ResponseRequirement(
                UUID.randomUUID().toString(),
                responseType,
                desc
        );
        r.setType(type);
        return r;
    }

    /**
     * Apply given Cardinality to given Requirement
     *
     * @param r The Requirement
     * @param c The Cardinality
     */
    private void applyCardinality(Requirement r, CardinalityEnum c) {
        r.setMandatory(c.isMandatory());
        r.setMultiple(c.isMultiple());
    }

    /**
     * Apply given Cardinality to given RequirementGroup
     *
     * @param rg The RequirementGroup
     * @param c  The Cardinality
     */
    private void applyCardinality(RequirementGroup rg, CardinalityEnum c) {
        rg.setMandatory(c.isMandatory());
        rg.setMultiple(c.isMultiple());
    }

    private RequirementGroupTypeEnum extractRequirementGroupType(String value) {

        switch (value) {

            case "QUESTION_GROUP}":
                return RequirementGroupTypeEnum.QUESTION_GROUP;

            case "QUESTION_SUBGROUP}":
                return RequirementGroupTypeEnum.QUESTION_SUBGROUP;

            case "REQUIREMENT_GROUP}":
                return RequirementGroupTypeEnum.REQUIREMENT_GROUP;

            case "REQUIREMENT_SUBGROUP}":
                return RequirementGroupTypeEnum.REQUIREMENT_SUBGROUP;

            default:
                return null;
        }

    }

    private RequirementTypeEnum extractRequirementType(String value) {

        switch (value) {

            case "{QUESTION}":
                return RequirementTypeEnum.QUESTION;

            case "{REQUIREMENT}":
                return RequirementTypeEnum.REQUIREMENT;

            case "{CAPTION}":
                return RequirementTypeEnum.CAPTION;

            default:
                return null;
        }

    }

    private List<Requirement> extractAllRequirementType(Sheet d, int rowNum, int endRowNum, int colNum) {
        List<Requirement> rList = new ArrayList<>();

        for (int i = rowNum; i < endRowNum; i++) {

            String cellValue = getCellStringValueOrNull(d.getRow(i), colNum);

            if ("{QUESTION}".equals(cellValue) ||
                    "{REQUIREMENT}".equals(cellValue) ||
                    "{CAPTION}".equals(cellValue)) {

                Requirement r = createResponseRequirementOfType(
                        ResponseTypeEnum.valueOf(getRowResponseType(d.getRow(i))),
                        getRowDescription(d.getRow(i)), //+ "(at " + i + "," + colNum + ")"
                        extractRequirementType(cellValue)
                );

                // set cardinality here
                CardinalityEnum c = TaxonomyDataUtils.extractCardinality(getRowCardinality(d.getRow(i)));
                applyCardinality(r, c);
                // set codelist
                r.setResponseValuesRelatedArtefact(getRowCodelist(d.getRow(i)));
                // set Property keys
                // The number of property keys depends on element. For example for an evidence compound element
                // there are 3 property keys.
                r.getPropertyKeyMap().put("pk1", getRowPropertyKey(d.getRow(i), 1));
                r.getPropertyKeyMap().put("pk2", getRowPropertyKey(d.getRow(i), 2));
                r.getPropertyKeyMap().put("pk3", getRowPropertyKey(d.getRow(i), 3));
                rList.add(r);
            }

        }
        return rList;
    }

    private String getRowUUID(Row r) {
        String UUID = getCellStringValueOrNull(r, elementUUIDColumnIndex);
        return UUID != null ? UUID.trim() : UUID;
    }

    private String getRowName(Row r) {
        return getCellStringValueOrNull(r, nameColumnIndex);
    }

    private String getRowDescription(Row r) {
        return getCellStringValueOrNull(r, descriptionColumnIndex);
    }

    private String getRowPropertyKey(Row r, int keyNumber) {

        switch (keyNumber) {

            case 1:
                return getCellStringValueOrNull(r, propertyKey1ColumnIndex);
            case 2:
                return getCellStringValueOrNull(r, propertyKey2ColumnIndex);
            case 3:
                return getCellStringValueOrNull(r, propertyKey3ColumnIndex);
            default:
                return null;
        }
    }

    private String getRowCode(Row r) {
        String elementCode = getCellStringValueOrNull(r, elementCodeColumnIndex);
        return elementCode != null ? elementCode.trim() : elementCode;
    }

    private String getRowResponseType(Row r) {
        return getCellStringValueOrNull(r, propertyDataTypeColumnIndex);
    }

    private String getRowCardinality(Row r) {
        return getCellStringOrNumericValueOrNull(r, cardinalityColumnIndex);
    }

    private String getRowCodelist(Row r) {
        return getCellStringValueOrNull(r, codelistColumnIndex);
    }

    private String getCellStringValueOrNull(Row r, int index) {
        Cell c = r.getCell(index);
        if (c == null) {
            return null;
        }
        return CellType.STRING.equals(c.getCellTypeEnum())
                ? c.getStringCellValue().trim()
                : null;
    }

    private String getCellStringOrNumericValueOrNull(Row r, int index) {
        Cell c = r.getCell(index);
        if (c == null) {
            return null;
        }

        if (CellType.STRING.equals(c.getCellTypeEnum())) {
            return c.getStringCellValue().trim();
        }

        if (CellType.NUMERIC.equals(c.getCellTypeEnum())) {
            return String.valueOf((int) c.getNumericCellValue());
        }

        return null;
    }

    @Override
    public List<SelectableCriterion> getCriterionList() {
        return getCriterionList(EULanguageCodeEnum.EN);
    }

    @Override
    public List<SelectableCriterion> getCriterionList(EULanguageCodeEnum lang) {

        // failsafe
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

        // failsafe
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

        // failsafe
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
