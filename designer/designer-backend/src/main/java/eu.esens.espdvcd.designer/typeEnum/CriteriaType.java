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
package eu.esens.espdvcd.designer.typeEnum;

public enum CriteriaType {
    EXCLUSION("^CRITERION.EXCLUSION.+"),
    EXCLUSION_A("^CRITERION.EXCLUSION.CONVICTIONS.+"),
    EXCLUSION_B("^CRITERION.EXCLUSION.CONTRIBUTIONS.+"),
    EXCLUSION_C("(^CRITERION.EXCLUSION.SOCIAL.+)|" +
            "(^CRITERION.EXCLUSION.BUSINESS.+)|" +
            "(^CRITERION.EXCLUSION.MISCONDUCT.+)|" +
            "(^CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.+)"),
    EXCLUSION_D("^CRITERION.EXCLUSION.NATIONAL.+"),
    ALL_SATISFIED("(.*ALL_SATISFIED*)|" +
            "(.*SELECTION.ALL*)"),
    SELECTION("^CRITERION.SELECTION.+"),
    SELECTION_NO_ALPHA("(?!.*ALL_SATISFIED*)(?!.*SELECTION.ALL*)^CRITERION.SELECTION.+"),
    SELECTION_A("^CRITERION.SELECTION.SUITABILITY.+"),
    SELECTION_B("^CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.+"),
    SELECTION_C("(?!.*CERTIFICATES*)^CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.+"),
    SELECTION_D("^CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.+"),
    EO_RELATED("((?!.*MEETS_THE_OBJECTIVE*)(?!.*REDUCTION_OF_CANDIDATES*))^CRITERION.OTHER.EO_DATA.+"),
    REDUCTION("(^CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE)|" +
            "(^CRITERION.OTHER.EO_DATA.REDUCTION_OF_CANDIDATES)"),
    EO_RELATED_A("(^CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST*)|" +
            "(^CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP*)|" +
            "(^CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS*)|" +
            "(^CRITERION.OTHER.EO_DATA.CONTRIBUTIONS_CERTIFICATES*)"),
    EO_RELATED_B("^CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES*"),
    EO_RELATED_C("^CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES*"),
    EO_LOTS("^CRITERION.OTHER.EO_DATA.LOTS_TENDERED"),
    OTHER_CA("^CRITERION.OTHER.CA_DATA.+");

    private final String regex;

    CriteriaType(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
