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

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetFromECertisTaskTest;
import eu.esens.espdvcd.retriever.criteria.resource.utils.AssertUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 21/11/2020.
 */
public class CriteriaDataRetrieverWiremockTest {

    private CriteriaDataRetriever retriever;

    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(options().port(9000).bindAddress("localhost"));

    @Before
    public void setUp() {
        retriever = new CriteriaDataRetrieverBuilder().build();
    }

    @Test
    public void testGetEUCriterionInDefaultLangWithWiremock() throws Exception {

        InputStream inputStream =
                GetFromECertisTaskTest.class.getResourceAsStream("/mock/ecertis/criteria/eu/eu-c1.json");

        Gson gson = new Gson();
        Object jsonObject = gson.fromJson(new InputStreamReader(inputStream), Object.class);
        String jsonResponse = gson.toJson(jsonObject);

        stubFor(
                get(urlEqualTo("/growth/tools-databases/ecertisrest3/criteria/espd/005eb9ed-1347-4ca3-bb29-9bc0db64e1ab?lang=en"))
                        .withHeader("Accept", equalTo("application/json"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(jsonResponse)));

        SelectableCriterion sc = retriever.getCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab");
        Assert.assertNotNull(sc);

        AssertUtils.assertLegislationAndItsFieldsNotNull(sc.getLegislationReference());
        // SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetEvidencesForEuropeanCriterionWithWiremock() throws Exception {

        InputStream inputStream =
                GetFromECertisTaskTest.class.getResourceAsStream("/mock/ecertis/criteria/eu/eu-c1.json");

        // Read the Criterion with Gson from resources as a String
        Gson gson = new Gson();
        Object jsonObject = gson.fromJson(new InputStreamReader(inputStream), Object.class);
        String jsonResponse = gson.toJson(jsonObject);

        stubFor(
                // This is the URL the getEvidencesForEuropeanCriterion will hit in the
                // back for EU Criterion with id = 005eb9ed-1347-4ca3-bb29-9bc0db64e1ab
                get(urlEqualTo("/growth/tools-databases/ecertisrest3/criteria/espd/005eb9ed-1347-4ca3-bb29-9bc0db64e1ab?lang=en"))
                        .withHeader("Accept", equalTo("application/json"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(jsonResponse)));

        // The list will be filled with Evidences that are included in /mock/ecertis/criteria/eu/eu-c1.json
        List<Evidence> evidenceList = retriever.getEvidencesForEuropeanCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab", null);

        // Check the number of Evidences retrieved back
        Assert.assertEquals(69, evidenceList.size());

        // Make sure that retrieved Evidences have all the data
        for (Evidence e : evidenceList) {
            AssertUtils.assertEvidenceAndItsFieldsNotNull(e);
        }

        // evidenceList.forEach(SelectableCriterionPrinter::printEvidence);
    }

    @Test
    public void testGetEvidencesForEuropeanCriterionByCountryCodeWithWiremock() throws Exception {

        InputStream inputStream =
                GetFromECertisTaskTest.class.getResourceAsStream("/mock/ecertis/criteria/eu/eu-c1.json");

        // Read the Criterion with Gson from resources as a String
        Gson gson = new Gson();
        Object jsonObject = gson.fromJson(new InputStreamReader(inputStream), Object.class);
        String jsonResponse = gson.toJson(jsonObject);

        stubFor(
                // This is the URL the getEvidencesForEuropeanCriterion will hit in the
                // back for EU Criterion with id = 005eb9ed-1347-4ca3-bb29-9bc0db64e1ab
                get(urlEqualTo("/growth/tools-databases/ecertisrest3/criteria/espd/005eb9ed-1347-4ca3-bb29-9bc0db64e1ab?lang=en"))
                        .withHeader("Accept", equalTo("application/json"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(jsonResponse)));

        String countryCode = ECertisNationalEntityEnum.FR.name();

        // The list will be filled with Evidences that are included in /mock/ecertis/criteria/eu/eu-c1.json
        List<Evidence> evidenceList = retriever.getEvidencesForEuropeanCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab", countryCode);

        // Check the number of Evidences retrieved back
        Assert.assertEquals(4, evidenceList.size());

        // Make sure that retrieved Evidences have all the data
        for (Evidence e : evidenceList) {
            AssertUtils.assertEvidenceAndItsFieldsNotNull(e);
        }

        // evidenceList.forEach(SelectableCriterionPrinter::printEvidence);
    }

}
