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
package eu.esens.espdvcd.codelist;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author konstantinos
 */
public enum CodelistsV1 implements Codelists {

    /**
     * Activity Type CodeList
     */
    ActivityType("/gc/v1/ActivityTypeCode-CodeList.gc"),
    /**
     * Amount Type CodeList
     */
    AmountType("/gc/v1/AmountTypeCodes-CodeList.gc"),
    /**
     * Contract Type CodeList
     */
    ContractType("/gc/v1/ContractType-CodeList.gc"),
    /**
     * Country Identification according to ISO 3A
     */
    CountryIdentification("/gc/v1/CountryCodeIdentifier-CodeList.gc"),
    /**
     * ESPD Criteria Type taxonomy, flattened as a Codelist
     */
    CriteriaType("/gc/v1/CriteriaTypeCodes-CodeList.gc"),
    /**
     * Criterion Jurisdiction Level for the ESPD Criteria Codelist
     */
    CriterionJurisdictionLevel("/gc/v1/CriterionJurisdictionLevel-CodeList.gc"),
    /**
     * UBL Cusomization ID codelist, used for the ESPD Request and Response
     * Artifacts
     */
    CustomizationID("/gc/v1/CustomizationID-CodeList.gc"),
    /**
     * Currency Codelist, ISO Compatible.
     */
    Currency("/gc/v1/CurrencyCode-CodeList.gc"),
    /**
     * Document Reference types, used by ESPD Request and Response to add
     * specific external references required by the ESPD Data Model
     */
    DocumentReferenceContentType("/gc/v1/DocRefContentType-CodeList.gc"),
    /**
     * Economic Operator Role Codelist
     */
    EORole("/gc/v1/EORoleCodes-CodeList.gc"),
    /**
     * Language iso code Codelist
     */
    LanguageCodeEU("/gc/v1/LanguageCodeEU-CodeList.gc"),
    /**
     * Response Data type codelist, used to infer the type of response a
     * requirement requires.
     */
    ResponseDataType("/gc/v1/ResponseDataType-CodeList.gc"),
    PeriodMeasureType("/gc/v1/PeriodMeasureTypeCodes-CodeList.gc"),
    PeriodType("/gc/v1/PeriodTypeCodes-CodeList.gc"),
    ProcedureType("/gc/v1/ProcedureType-CodeList.gc"),
    ProfileExecutionID("/gc/v1/ProfileExecutionID-CodeList.gc"),
    ProjectType("/gc/v1/ProjectType-CodeList.gc"),
    ServicesProjectSubType("/gc/v1/ServicesProjectSubType-CodeList.gc"),
    TechnicalCapabilityType("/gc/v1/TechnicalCapabilityTypeCode-CodeList.gc"),
    TenderingRole("/gc/v1/TendererRole-CodeList.gc");

    private final String name;
    private volatile GenericCode INSTANCE;
    private static final String DEFAULT_LANG = "en";

    CodelistsV1(String s) {
        name = s;
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

    /**
     * @param id
     * @return true if the codelist contains the specific id, false otherwise
     */
    @Override
    public final boolean containsId(String id) {
        return getInstance().containsId(id, DEFAULT_LANG);
    }

    /**
     * @param value
     * @return true if the codelist contains the specific value, false otherwise
     */
    @Override
    public final boolean containsValue(String value) {
        return getInstance().containsValue(value, DEFAULT_LANG);
    }

    /**
     * @param id
     * @return the value mapped with the specific id in the codelist, null
     * otherwise
     */
    @Override
    public final String getValueForId(String id) {
        return getInstance().getValueForId(id, DEFAULT_LANG);
    }

    /**
     * @return the internal representation of the codelist as an immutable bimap
     */
    @Override
    public final Map<String, String> getDataMap() {
        return getInstance().getDataMap(DEFAULT_LANG);
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

    @Override
    public Optional<Map<String, String>> _getDataMap() {
        return getInstance()._getDataMap(DEFAULT_LANG);
    }

    @Override
    public Optional<String> _getValueForId(String id) {
        return getInstance()._getValueForId(id, DEFAULT_LANG);
    }

    /**
     * @return All available languages
     */
    Set<String> getAllLang() {
        return getInstance().getAllLang();
    }

}
