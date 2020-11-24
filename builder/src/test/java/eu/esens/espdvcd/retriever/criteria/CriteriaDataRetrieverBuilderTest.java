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

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import eu.esens.espdvcd.retriever.criteria.resource.utils.AssertUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverBuilderTest {

    private CriteriaDataRetriever retriever;

    @Before
    public void setUp() {
        retriever = new CriteriaDataRetrieverBuilder().build();
    }

    @Test
    public void testGetEuCriterionInDefaultLang() throws Exception {

        final String id = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";

        SelectableCriterion sc = retriever.getCriterion(id);
        Assert.assertNotNull(sc);

        AssertUtils.assertLegislationAndItsFieldsNotNull(sc.getLegislationReference());
        // SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetNationalCriterionWithGreekLangParam() throws Exception {

        final String id = "14df34e8-15a9-411c-8c05-8c051693e277";

        retriever.setLang(EULanguageCodeEnum.EL);
        SelectableCriterion sc = retriever.getCriterion(id);
        Assert.assertNotNull(sc);

        AssertUtils.assertLegislationAndItsFieldsNotNull(sc.getLegislationReference());
        // SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetNationalCriterionWithGermanLangParam() throws Exception {

        final String id = "14df34e8-15a9-411c-8c05-8c051693e277";

        retriever.setLang(EULanguageCodeEnum.DE);
        SelectableCriterion sc = retriever.getCriterion(id);
        Assert.assertNotNull(sc);

        AssertUtils.assertLegislationAndItsFieldsNotNull(sc.getLegislationReference());
        // SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testEuToNationalMappingInDefaultLang() throws Exception {

        final String id = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
        final String countryCode = ECertisNationalEntityEnum.DE.name();

        List<SelectableCriterion> mappedCriteriaList = retriever.getNationalCriterionMapping(id, countryCode);

        for (SelectableCriterion mappedCriterion : mappedCriteriaList) {
            AssertUtils.assertLegislationAndItsFieldsNotNull(mappedCriterion.getLegislationReference());
            mappedCriterion.getEvidenceList().forEach(e -> AssertUtils.assertEvidenceAndItsFieldsNotNull(e));
            // SelectableCriterionPrinter.print(mappedCriterion);
        }
    }

    @Test
    public void testEuToNationalMappingWithLangParam() throws Exception {

        final String id = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
        final String countryCode = ECertisNationalEntityEnum.GR.name();

        retriever.setLang(EULanguageCodeEnum.EL);
        List<SelectableCriterion> mappedCriteriaList = retriever.getNationalCriterionMapping(id, countryCode);

        for (SelectableCriterion mappedCriterion : mappedCriteriaList) {
            AssertUtils.assertLegislationAndItsFieldsNotNull(mappedCriterion.getLegislationReference());
            mappedCriterion.getEvidenceList().forEach(e -> AssertUtils.assertEvidenceAndItsFieldsNotNull(e));
            // SelectableCriterionPrinter.print(mappedCriterion);
        }
    }

    @Test
    public void testNationalToNationalMappingInDefaultLang() throws Exception {

        final String id = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
        final String countryCode = ECertisNationalEntityEnum.HU.name();

        List<SelectableCriterion> mappedCriteriaList = retriever.getNationalCriterionMapping(id, countryCode);

        for (SelectableCriterion mappedCriterion : mappedCriteriaList) {
            AssertUtils.assertLegislationAndItsFieldsNotNull(mappedCriterion.getLegislationReference());
            mappedCriterion.getEvidenceList().forEach(e -> AssertUtils.assertEvidenceAndItsFieldsNotNull(e));
            // SelectableCriterionPrinter.print(mappedCriterion);
        }
    }

    @Test
    public void testNationalToNationalMappingWithLangParam() throws Exception {

        final String id = "14df34e8-15a9-411c-8c05-8c051693e277";
        final String countryCode = ECertisNationalEntityEnum.HU.name();

        retriever.setLang(EULanguageCodeEnum.DE);
        List<SelectableCriterion> mappedCriteriaList = retriever.getNationalCriterionMapping(id, countryCode);

        for (SelectableCriterion mappedCriterion : mappedCriteriaList) {
            AssertUtils.assertLegislationAndItsFieldsNotNull(mappedCriterion.getLegislationReference());
            mappedCriterion.getEvidenceList().forEach(e -> AssertUtils.assertEvidenceAndItsFieldsNotNull(e));
            // SelectableCriterionPrinter.print(mappedCriterion);
        }
    }

    @Test
    public void testGetEvidencesForNationalCriterionInDefaultLang() throws Exception {

        final String id = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";

        List<Evidence> eList = retriever.getEvidencesForNationalCriterion(id);
        Assert.assertFalse(eList.isEmpty());

        for (Evidence e : eList) {
            AssertUtils.assertEvidenceAndItsFieldsNotNull(e);
            // SelectableCriterionPrinter.printEvidence(e);
        }

    }

    @Test
    public void testGetEvidencesForEuropeanCriterionInDefaultLang() throws Exception {

        final String id = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
        final String countryCode = ECertisNationalEntityEnum.GR.name();

        List<Evidence> eList = retriever.getEvidencesForEuropeanCriterion(id, countryCode);
        Assert.assertFalse(eList.isEmpty());

        for (Evidence e : eList) {
            AssertUtils.assertEvidenceAndItsFieldsNotNull(e);
            // SelectableCriterionPrinter.printEvidence(e);
        }
    }

    @Test
    public void testGetEvidencesForNationalCriterionWithLangParam() throws Exception {

        final String id = "14df34e8-15a9-411c-8c05-8c051693e277";
        retriever.setLang(EULanguageCodeEnum.EL);

        List<Evidence> eList = retriever.getEvidencesForNationalCriterion(id);
        Assert.assertFalse(eList.isEmpty());

        for (Evidence e : eList) {
            AssertUtils.assertEvidenceAndItsFieldsNotNull(e);
            // SelectableCriterionPrinter.printEvidence(e);
        }
    }

    @Test
    public void testGetEvidencesForEuropeanCriterionWithLangParam() throws Exception {

        final String id = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
        final String countryCode = ECertisNationalEntityEnum.DE.name();
        retriever.setLang(EULanguageCodeEnum.EL);

        List<Evidence> eList = retriever.getEvidencesForEuropeanCriterion(id, countryCode);
        Assert.assertFalse(eList.isEmpty());

        for (Evidence e : eList) {
            AssertUtils.assertEvidenceAndItsFieldsNotNull(e);
            // SelectableCriterionPrinter.printEvidence(e);
        }
    }

}
