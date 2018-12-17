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

import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ESPDArtefactResourceTest {

    private ESPDArtefactResource r1;
    private ESPDArtefactResource r2;

    @Before
    public void setUp() {
        r1 = new ESPDArtefactResource(EDMVersion.V1);
        Assert.assertNotNull(r1);
        r2 = new ESPDArtefactResource(EDMVersion.V2);
        Assert.assertNotNull(r2);
    }

    @Test
    public void testGetFullListV1() {
        SelectableCriterionPrinter.print(r1.getCriterionList());
    }

    @Test
    public void testGetFullListV2() {
        SelectableCriterionPrinter.print(r2.getCriterionList());
    }

    @Test
    public void testGetFullListByTypeCodeV1() {
//        Assert.assertTrue(r1.getCriterionList().stream()
//                .map(sc -> sc.getTypeCode())
//                .collect(Collectors.toList())
//                .containsAll(createCriteriaTypeCodeListV1()));
//
//        Assert.assertTrue(createCriteriaTypeCodeListV1()
//                .containsAll(r1.getCriterionList().stream()
//                        .map(sc -> sc.getTypeCode())
//                        .collect(Collectors.toList())));

//        Assert.assertEquals(r1.getCriterionList().stream()
//                .map(sc -> sc.getTypeCode())
//                .collect(Collectors.toSet()), createCriteriaTypeCodeSetV1());

//        Assert.assertTrue(r1.getCriterionList().stream()
//                .map(sc -> sc.getTypeCode())
//                .collect(Collectors.toSet()).equals(createCriteriaTypeCodeSetV1()));

        Set<String> taxonomyTypeCodeSet = createCriteriaTypeCodeSetV1();
        Set<String> resourceTypeCodeSet = r1.getCriterionList().stream()
                .map(sc -> sc.getTypeCode())
                .collect(Collectors.toSet());

        System.out.println("TypeCodes that exist in Resource List and does not exist in Taxonomy List");

        resourceTypeCodeSet.forEach(typeCode -> {

            if (!taxonomyTypeCodeSet.contains(typeCode)) {
                System.out.println(typeCode);
            }

        });

        System.out.println("TypeCodes that exist in Taxonomy List and does not exist in Resource List");

        taxonomyTypeCodeSet.forEach(typeCode -> {

            if (!resourceTypeCodeSet.contains(typeCode)) {
                System.out.println(typeCode);
            }

        });

    }

    private Set<String> createCriteriaTypeCodeSetV1() {

        Set<String> set = new HashSet<>();

        // From taxonomy
        set.add("CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION"); // 1
        set.add("CRITERION.EXCLUSION.CONVICTIONS.CORRUPTION"); // 2
        set.add("CRITERION.EXCLUSION.CONVICTIONS.FRAUD"); // 3
        set.add("CRITERION.EXCLUSION.CONVICTIONS.TERRORIST_OFFENCES"); // 4
        set.add("CRITERION.EXCLUSION.CONVICTIONS.MONEY_LAUNDERING"); // 5
        set.add("CRITERION.EXCLUSION.CONVICTIONS.CHILD_LABOUR-HUMAN_TRAFFICKING"); // 6
        set.add("CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_TAXES"); // 7
        set.add("CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_SOCIAL_SECURITY"); // 8
        set.add("CRITERION.EXCLUSION.SOCIAL.ENVIRONMENTAL_LAW"); // 9
        set.add("CRITERION.EXCLUSION.SOCIAL.SOCIAL_LAW"); // 10
        set.add("CRITERION.EXCLUSION.SOCIAL.LABOUR_LAW"); // 11
        set.add("CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY"); // 12
        set.add("CRITERION.EXCLUSION.BUSINESS.INSOLVENCY"); // 13
        set.add("CRITERION.EXCLUSION.BUSINESS.CREDITORS_ARRANGEMENT"); // 14
        set.add("CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY_ANALOGOUS"); // 15
        set.add("CRITERION.EXCLUSION.BUSINESS.LIQUIDATOR_ADMINISTERED"); // 16
        set.add("CRITERION.EXCLUSION.BUSINESS.ACTIVITIES_SUSPENDED"); // 17
        set.add("CRITERION.EXCLUSION.MISCONDUCT.MC_PROFESSIONAL"); // 18
        set.add("CRITERION.EXCLUSION.MISCONDUCT.MARKET_DISTORTION"); // 19
        set.add("CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PARTICIPATION"); // 20
        set.add("CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PREPARATION"); // 21
        set.add("CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.EARLY_TERMINATION"); // 22
        set.add("CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.MISINTERPRETATION"); // 23
        set.add("CRITERION.EXCLUSION.NATIONAL.OTHER"); // 24
        set.add("CRITERION.SELECTION.SUITABILITY.PROFESSIONAL_REGISTER_ENROLMENT"); // 25
        set.add("CRITERION.SELECTION.SUITABILITY.TRADE_REGISTER_ENROLMENT"); // 26
        set.add("CRITERION.SELECTION.SUITABILITY.AUTHORISATION"); // 27
        set.add("CRITERION.SELECTION.SUITABILITY.MEMBERSHIP"); // 28
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.GENERAL_YEARLY"); // 29
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.AVERAGE_YEARLY"); // 30
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_YEARLY"); // 31
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_AVERAGE"); // 32
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SET_UP"); // 33
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.FINANCIAL_RATIO"); // 34
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.RISK_INDEMNITY_INSURANCE"); // 35
        set.add("CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.OTHER_REQUIREMENTS"); // 36
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.WORKS_PERFORMANCE"); // 37
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SUPPLIES_DELIVERY_PERFORMANCE"); // 38
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SERVICES_DELIVERY_PERFORMANCE"); // 39
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_QUALITY_CONTROL"); // 40
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_CARRYING_WORKS"); // 41
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_QUALITY_ENSURING"); // 42
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES.FOR_STUDY_RESEARCH"); // 43
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.SUPPLY_CHAIN_MANAGEMENT"); // 44
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.CHECKS.ALLOWANCE_OF_CHECKS"); // 45
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.PROFESSIONAL.QUALIFICATIONS");  // 46
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.ENVIRONMENTAL_MANAGEMENT_MEASURES"); // 47
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.MANAGERIAL_STAFF"); // 48
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.AVERAGE_ANNUAL_MANPOWER"); // 49
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.EQUIPMENT"); // 50
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.SUBCONTRACTING_PROPORTION"); // 51
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.NO_AUTHENTICATED_ARTEFACTS"); // 52
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.AUTHENTICATED_ARTEFACTS"); // 53
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INSTITUTES_CERTIFICATE"); // 54
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE"); // 55
        set.add("CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE"); // 56
        set.add("CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP"); // 57
        set.add("CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST"); // 58
        set.add("CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS"); // 59
        set.add("CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES"); // 60
        set.add("CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES"); // 61
        set.add("CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE"); // 62
        // ALL_SATISFIED
        set.add("CRITERION.SELECTION.ALL_SATISFIED"); // 63

        return set;
    }

}
