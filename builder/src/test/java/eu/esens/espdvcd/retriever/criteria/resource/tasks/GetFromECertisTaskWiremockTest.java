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
package eu.esens.espdvcd.retriever.criteria.resource.tasks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 20/11/2020.
 */
public class GetFromECertisTaskWiremockTest {

    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(options().port(9000).bindAddress("localhost"));

    InputStream inputStream =
            GetFromECertisTaskWiremockTest.class.getResourceAsStream("/mock/ecertis/criteria/eu/eu-c1.json");

    @Test
    public void testCall() throws Exception {

        Gson gson = new Gson();
        Object jsonObject = gson.fromJson(new InputStreamReader(inputStream), Object.class);
        String jsonResponse = gson.toJson(jsonObject);

        stubFor(
                get(urlEqualTo("/c1"))
                        .withHeader("Accept", equalTo("application/json"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(jsonResponse)));

        GetFromECertisTask task = new GetFromECertisTask(URI.create("http://localhost:9000/c1"));
        String stringResponse = task.call();

        // System.out.println(stringResponse);
        Assert.assertEquals(jsonResponse, stringResponse);
    }

}
