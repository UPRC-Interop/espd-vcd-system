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
package eu.esens.espdvcd.retriever.criteria.resource.tasks;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis
 */
public class GetFromECertisTask implements Callable<String> {

    private static final Logger LOGGER = Logger.getLogger(GetFromECertisTask.class.getName());
    private final String url;

    public GetFromECertisTask(String url) {
        this.url = url;
    }

    @Override
    public String call() throws IOException {

        // LOGGER.log(Level.INFO, String.format("%-16s Task: %s START", Thread.currentThread().getName(), url));
        System.out.printf("%-16s Task: %s START\n", Thread.currentThread().getName(), url);
        long startTime = System.currentTimeMillis();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();

                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    long endTime = System.currentTimeMillis();
                    // LOGGER.log(Level.INFO, String.format("%-16s Task: %s FINISH %d ms", Thread.currentThread().getName(), url, (endTime - startTime)));
                    System.out.printf("%-16s Task: %s FINISH %d ms\n", Thread.currentThread().getName(), url, (endTime - startTime));
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    long endTime = System.currentTimeMillis();
                    // LOGGER.log(Level.SEVERE, String.format("%-16s Task: %s FINISH with exception %d ms", Thread.currentThread().getName(), url, (endTime - startTime)));
                    System.out.printf("%-16s Task: %s FINISH with exception %d ms\n", Thread.currentThread().getName(), url, (endTime - startTime));
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            };

            return httpClient.execute(httpGet, responseHandler);
        }

    }

}
