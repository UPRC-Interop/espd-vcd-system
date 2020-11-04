/**
 * Copyright 2016-2020 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.RegulatedCriteriaTaxonomyResource;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverBuilderTest {


    private CriteriaDataRetriever retriever;
    private CriteriaTaxonomyResource regulatedTaxonomy;
    private String firstCriterionId;
    private static boolean setUpIsDone = false;


    @Before
    public void setUp() {
        retriever = new CriteriaDataRetrieverBuilder().build();
        if (setUpIsDone) {
            return;
        }
        regulatedTaxonomy = new RegulatedCriteriaTaxonomyResource();
        firstCriterionId = regulatedTaxonomy.getCriterionList().get(0).getID();
        setUpIsDone = true;
    }

    @Test
    public void testGetCriterionForDefaultLang() throws Exception {
        SelectableCriterion sc = retriever.getCriterion(firstCriterionId);
        Assert.assertNotNull(sc);
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetNationalCriterionWithGreekLangParam() throws Exception {
        retriever.setLang(EULanguageCodeEnum.EL);
        SelectableCriterion sc = retriever.getCriterion(firstCriterionId);
        Assert.assertNotNull(sc);
        Assert.assertNotNull(sc.getLegislationReference());
        Assert.assertNotNull(sc.getLegislationReference().getJurisdictionLevelCode());
        Assert.assertNotNull(sc.getLegislationReference().getDescription());
        Assert.assertNotNull(sc.getLegislationReference().getURI());
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetNationalCriterionWithGermanLangParam() throws Exception {
        retriever.setLang(EULanguageCodeEnum.DE);
        SelectableCriterion sc = retriever.getCriterion(firstCriterionId);
        Assert.assertNotNull(sc);
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testEuToNationalMapping() throws Exception {

        if (ResourceConfig.INSTANCE.useProduction()) {
            String ID = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";

            String code = EULanguageCodeEnum.DE.name();

            List<String> ncList = retriever.getNationalCriterionMapping(ID, code)
                    .stream()
                    .map(c -> c.getID())
                    .collect(Collectors.toList());

            System.out.printf("European Criterion with ID %s mapped to National (%s) Criterion/s --> %s\n"
                    , ID, CodelistsV2.LanguageCodeEU.getValueForId(code), ncList);
        } else {
            System.out.println("CriteriaDataRetrieverBuilderTest.testEuToNationalMapping has been ignored, " +
                    "because Multi Domain e-Certis does not have national criteria");
        }
    }

    @Test
    public void testEuToNationalMappingWithLangParam() throws Exception {

        if (ResourceConfig.INSTANCE.useProduction()) {
            String ID = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
            // String code = EULanguageCodeEnum.DE.name();
            String code = "gr";

            retriever.setLang(EULanguageCodeEnum.EL);
            // r.setLang(EULanguageCodeEnum.DE);

            SelectableCriterionPrinter.print(retriever.getNationalCriterionMapping(ID, code));
        } else {
            System.out.println("CriteriaDataRetrieverBuilderTest.testEuToNationalMappingWithLangParam has been ignored, " +
                    "because Multi Domain e-Certis does not have national criteria");
        }
    }

    @Test
    public void testNationalToNationalMapping() throws Exception {

        if (ResourceConfig.INSTANCE.useProduction()) {
            String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
            String code = EULanguageCodeEnum.HU.name();

            List<String> ncList = retriever.getNationalCriterionMapping(ID, code)
                    .stream()
                    .map(c -> c.getID())
                    .collect(Collectors.toList());

            System.out.printf("National Criterion with ID %s mapped to National (%s) Criterion/s --> %s\n"
                    , ID, CodelistsV2.LanguageCodeEU.getValueForId(code), ncList);
        } else {
            System.out.println("CriteriaDataRetrieverBuilderTest.testEuToNationalMappingWithLangParam has been ignored, " +
                    "because Multi Domain e-Certis does not have national criteria");
        }
    }

    @Test
    public void testNationalToNationalMappingWithLangParam() throws Exception {

        if (ResourceConfig.INSTANCE.useProduction()) {
            String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
            String code = EULanguageCodeEnum.DE.name();

            retriever.setLang(EULanguageCodeEnum.DE);

            SelectableCriterionPrinter.print(retriever.getNationalCriterionMapping(ID, code));
        } else {
            System.out.println("CriteriaDataRetrieverBuilderTest.testEuToNationalMappingWithLangParam has been ignored, " +
                    "because Multi Domain e-Certis does not have national criteria");
        }
    }

    @Test
    public void testGetEvidencesForDefaultLang() throws Exception {

        if (ResourceConfig.INSTANCE.useProduction()) {
            final String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";

            List<Evidence> eList = retriever.getEvidences(ID);
            Assert.assertFalse(eList.isEmpty());

            int index = 1;

            for (Evidence e : eList) {
                System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
                // SelectableCriterionPrinter.printEvidence(e);
            }
        } else { //  Multi-Domain e-Certis evidences
            retriever.setCountryFilter(ECertisNationalEntityEnum.GR);
            List<Evidence> eList = retriever.getEvidences(firstCriterionId);
            Assert.assertFalse(eList.isEmpty());

            Evidence firstEvidence = eList.get(0);
            Assert.assertNotNull(firstEvidence.getID());
            Assert.assertNotNull(firstEvidence.getDescription());
            Assert.assertNotNull(firstEvidence.getEvidenceURL());

//            int index = 1;
//
//            for (Evidence e : eList) {
//                // System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
//                SelectableCriterionPrinter.printEvidence(e);
//            }
        }

    }

    @Test
    public void testGetEvidencesWithLangParam() throws Exception {

        if (ResourceConfig.INSTANCE.useProduction()) {
            final String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
            retriever.setLang(EULanguageCodeEnum.EL);

            List<Evidence> eList = retriever.getEvidences(ID);
            Assert.assertFalse(eList.isEmpty());

            int index = 1;

            for (Evidence e : retriever.getEvidences(ID)) {
                System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
            }
        } else { //  Multi-Domain e-Certis evidences
            retriever.setLang(EULanguageCodeEnum.EL);
            List<Evidence> eList = retriever.getEvidences(firstCriterionId);
            Assert.assertFalse(eList.isEmpty());

            Evidence firstEvidence = eList.get(0);
            Assert.assertNotNull(firstEvidence.getID());
            Assert.assertNotNull(firstEvidence.getDescription());
            Assert.assertNotNull(firstEvidence.getEvidenceURL());

//            int index = 1;
//
//            for (Evidence e : eList) {
//                // System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
//                SelectableCriterionPrinter.printEvidence(e);
//            }
        }

    }

    @Test
    public void testGetEvidencesWithNationalEntityAndLangParam() throws Exception {

        if (!ResourceConfig.INSTANCE.useProduction()) { //  Multi-Domain e-Certis evidences
            retriever.setLang(EULanguageCodeEnum.EL);
            retriever.setCountryFilter(ECertisNationalEntityEnum.GR);
            List<Evidence> eList = retriever.getEvidences(firstCriterionId);
            Assert.assertFalse(eList.isEmpty());

            Evidence firstEvidence = eList.get(0);
            Assert.assertNotNull(firstEvidence.getID());
            Assert.assertNotNull(firstEvidence.getDescription());
            Assert.assertNotNull(firstEvidence.getEvidenceURL());

//            int index = 1;
//
//            for (Evidence e : eList) {
//                // System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
//                SelectableCriterionPrinter.printEvidence(e);
//            }
        }

    }

}
