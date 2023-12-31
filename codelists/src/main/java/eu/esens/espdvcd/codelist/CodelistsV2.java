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
package eu.esens.espdvcd.codelist;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Konstantinos Raptis
 */
public enum CodelistsV2 implements MultilingualCodelists {

    /**
     * Bid Type CodeList
     */
    BidType("/gc/v2/BidType-CodeList.gc"),
    /**
     * Set whether an answer to a Criterion Property or/and Evidence can be publicly accessible or not
     */
    ConfidentialityLevel("/gc/v2/ConfidentialityLevel-CodeList.gc"),
    /**
     * Country Identification according to ISO 3A
     */
    CountryIdentification("/gc/v2/country-v2-translated.gc"),
    /**
     * Criterion Element Type CodeList
     */
    CriterionElementType("/gc/v2/CriterionElementType-CodeList.gc"),
    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/v2/CurrencyCode-CodeList.gc"),
    /**
     * Document Reference types, used by ESPD Request and Response to add specific external references required by
     * the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/v2/DocRefContentType-CodeList.gc"),
    /**
     * States the type of identifier of an Economic Operator of a group of tenderers
     */
    EOIDType("/gc/v2/EOIDType-CodeList.gc"),
    /**
     * Economic Operator Industry Classification CodeList
     */
    EOIndustryClassification("/gc/v2/EOIndustryClassificationCode-CodeList.gc"),
    /**
     * Language iso code Codelist
     */
    LanguageCodeEU("/gc/v2/EULanguageCode-CodeList.gc"),
    /**
     * Specifies whether the Criterion is weigthed or a pass/fail one
     */
    EvaluationMethodType("/gc/v2/EvaluationMethodType-CodeList.gc"),
    /**
     * Financial Ratio Type CodeList
     */
    FinancialRatioType("/gc/v2/FinancialRatioType-CodeList.gc"),
    /**
     * Qualification Application Type, created in order to specify whether the ESPD is "Regulated" or
     * "Self-contained"
     */
    QualificationApplicationType("/gc/v2/QualificationApplicationType-CodeList.gc"),
    /**
     * Criteria Taxonomy Codelists contains criteria typeCodes and description
     */
//    ESPD_CriteriaTaxonomy_V1_0_2("/gc/v2/ESPD-CriteriaTaxonomy_V1.0.2.gc"),
//    ESPD_CriteriaTaxonomy_V1_0_3("/gc/v2/ESPD-CriteriaTaxonomy_V1.0.3.gc"),
//    ESPD_CriteriaTaxonomy_V2_0_2("/gc/v2/ESPD-CriteriaTaxonomy_V2.0.2.gc"),
    /**
     *
     */
    WeightingType("/gc/v2/WeightingType-CodeList.gc"),
    /**
     * Response Data type codelist, used to infer the type of response a requirement requires.
     */
    ResponseDataType("/gc/v2/ResponseDataType-CodeList.gc"),
    ProcedureType("/gc/v2/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/v2/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/v2/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/v2/ServicesProjectSubType-CodeList.gc"),
    EORoleType("/gc/v2/EORoleType-CodeList.gc"),
    PropertyGroupType("/gc/v2/PropertyGroupType-CodeList.gc");

    private final String name;
    private volatile GenericCode INSTANCE;

    private static final String DEFAULT_LANG = "eng";

    CodelistsV2(String name) {
        this.name = name;
    }

    private GenericCode getInstance() {
        if (INSTANCE == null) {
            makeInstance();
        }
        return INSTANCE;
    }

    protected synchronized void makeInstance() {
        INSTANCE = new GenericCode(name, DEFAULT_LANG);
    }

    /**
     * @param otherName
     * @return true if the name of the codelist is equal with @param othername
     */
    @Override
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * This method work as wrapper for enum name() method
     *
     * @return the exact name of this ENUM_CONSTANT
     */
    @Override
    public String getConstantName() {
        return name();
    }

    /**
     * @param id
     * @return the value mapped with the specific id in the codelist, null otherwise
     */
    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id, DEFAULT_LANG);
    }

    @Override
    public String getValueForId(String id, String lang) {
        return getInstance().getValueForId(id, lang);
    }

    /**
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id, DEFAULT_LANG);
    }

    /**
     * @param id
     * @param lang
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public boolean containsId(String id, String lang) {
        return getInstance().containsId(id, lang);
    }

    /**
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value, DEFAULT_LANG);
    }

    @Override
    public boolean containsValue(String value, String lang) {
        return getInstance().containsValue(value, lang);
    }

    /**
     * @return the internal representation of the codelist as an immutable map
     */
    @Override
    public final Map<String, String> getDataMap() {
        return getInstance().getDataMap(DEFAULT_LANG);
    }

    /**
     * @param lang The language in which the data will be
     * @return the internal representation of the codelist as an immutable map
     */
    @Override
    public Map<String, String> getDataMap(String lang) {
        return getInstance().getDataMap(lang);
    }

    @Override
    public Optional<Map<String, String>> _getDataMap() {
        return _getDataMap(DEFAULT_LANG);
    }

    @Override
    public Optional<Map<String, String>> _getDataMap(String lang) {
        return getInstance()._getDataMap(lang);
    }

    @Override
    public Optional<String> _getValueForId(String id) {
        return _getValueForId(id, DEFAULT_LANG);
    }

    @Override
    public Optional<String> _getValueForId(String id, String lang) {
        return getInstance()._getValueForId(id, lang);
    }

    /**
     * @return All available languages
     */
    Set<String> getAllLangs() {
        return getInstance().getAllLang();
    }

}
