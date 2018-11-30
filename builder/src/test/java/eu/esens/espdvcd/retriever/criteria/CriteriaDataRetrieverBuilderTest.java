/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverBuilderTest {


    private CriteriaDataRetriever r;

    @Before
    public void setUp() {
        r = new CriteriaDataRetrieverBuilder().build();
    }

    @Test
    public void testGetCriterionForDefaultLang() throws Exception {

        SelectableCriterion sc = r.getCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetNationalCriterionWithGreekLangParam() throws Exception {
        r.setLang(EULanguageCodeEnum.EL);

        SelectableCriterion sc = r.getCriterion("14df34e8-15a9-411c-8c05-8c051693e277");
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetNationalCriterionWithGermanLangParam() throws Exception {
        r.setLang(EULanguageCodeEnum.DE);

        SelectableCriterion sc = r.getCriterion("bf5baa60-98fe-4832-af84-de8451b0f17f");
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testEuToNationalMapping() throws Exception {

        String ID = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";


        String code = EULanguageCodeEnum.DE.name();

        List<String> ncList = r.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID())
                .collect(Collectors.toList());

        System.out.printf("European Criterion with ID %s mapped to National (%s) Criterion/s --> %s\n"
                , ID, CodelistsV2.LanguageCodeEU.getValueForId(code), ncList);
    }

    @Test
    public void testEuToNationalMappingWithLangParam() throws Exception {

        String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
        String code = EULanguageCodeEnum.DE.name();

        r.setLang(EULanguageCodeEnum.DE);

        SelectableCriterionPrinter.print(r.getNationalCriterionMapping(ID, code));
    }

    @Test
    public void testNationalToNationalMapping() throws Exception {

        String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
        String code = EULanguageCodeEnum.HU.name();

        List<String> ncList = r.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID())
                .collect(Collectors.toList());

        System.out.printf("National Criterion with ID %s mapped to National (%s) Criterion/s --> %s\n"
                , ID, CodelistsV2.LanguageCodeEU.getValueForId(code), ncList);
    }

    @Test
    public void testNationalToNationalMappingWithLangParam() throws Exception {

        String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
        String code = EULanguageCodeEnum.DE.name();

        r.setLang(EULanguageCodeEnum.DE);

        SelectableCriterionPrinter.print(r.getNationalCriterionMapping(ID, code));
    }

    @Test
    public void testGetEvidencesForDefaultLang() throws Exception {

        final String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";

        List<Evidence> eList = r.getEvidences(ID);
        Assert.assertFalse(eList.isEmpty());

        int index = 1;

        for (Evidence e : r.getEvidences(ID)) {
            System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
        }

    }

    @Test
    public void testGetEvidencesWithLangParam() throws Exception {

        final String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
        r.setLang(EULanguageCodeEnum.EL);

        List<Evidence> eList = r.getEvidences(ID);
        Assert.assertFalse(eList.isEmpty());

        int index = 1;

        for (Evidence e : r.getEvidences(ID)) {
            System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
        }

    }

}
