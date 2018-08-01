package eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetFromECertisTask implements Callable<String> {

    private static final Logger LOGGER = Logger.getLogger(GetFromECertisTask.class.getName());
    private final String url;

    public GetFromECertisTask(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {

        LOGGER.log(Level.INFO, String.format("%-16s Task: %s START", Thread.currentThread().getName(), url));
        long startTime = System.currentTimeMillis();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();

                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    long endTime = System.currentTimeMillis();
                    LOGGER.log(Level.INFO, String.format("%-16s Task: %s FINISH %d ms", Thread.currentThread().getName(), url, (endTime - startTime)));
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    long endTime = System.currentTimeMillis();
                    LOGGER.log(Level.SEVERE, String.format("%-16s Task: %s FINISH with exception %d ms", Thread.currentThread().getName(), url, (endTime - startTime)));
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            };

            return httpClient.execute(httpGet, responseHandler);
        }

    }

}
