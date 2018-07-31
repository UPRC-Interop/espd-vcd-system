package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class ECertisResource implements CriteriaResource, LegislationResource {

    private static final String ECERTIS_URL = "https://ec.europa.eu/growth/tools-databases/ecertisrest";
    private static final String ALL_CRITERIA = ECERTIS_URL + "/criteria";

    // Jackson related Errors
    private static final String ERROR_INVALID_CONTENT = "Error... JSON Input Contains Invalid Content";
    private static final String ERROR_UNEXPECTED_STRUCTURE = "Error... JSON Structure does not Match Structure Expected";

    // Contains all eu criteria
    Map<String, ECertisCriterion> criterionMap;

    Logger logger = Logger.getLogger(ECertisResource.class.getName());

    public ECertisResource() {

    }

    /**
     * Apply EU criteria to criterionMap
     *
     * @throws RetrieverException
     */
    private void initCriterionMap() throws RetrieverException {

        if (criterionMap == null) {
            criterionMap = createECertisCriterionMap();
        }
    }

    Map<String, ECertisCriterion> createECertisCriterionMap() throws RetrieverException {

        Map<String, ECertisCriterion> eCertisCriterionMap = new LinkedHashMap<>();

        List<String> fullIDList = getAllEUCriteriaID();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Set<GetECertisCriterionTask> tasks = new LinkedHashSet<>(fullIDList.size());
        fullIDList.forEach(ID -> tasks.add(new GetECertisCriterionTask(ID)));

        try {
            List<Future<ECertisCriterion>> futureList = executor.invokeAll(tasks);

            for (Future f : futureList) {

                if (f.isDone()) {
                    ECertisCriterion c = (ECertisCriterion) f.get();
                    eCertisCriterionMap.put(c.getID(), c);
                }

            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RetrieverException(e);
        }

        return eCertisCriterionMap;
    }

    private static class GetECertisCriterionTask implements Callable<ECertisCriterion> {

        private String ID;
        private String lang;

        public GetECertisCriterionTask(String ID) {
            this(ID, EULanguageCodeEnum.EN);
        }

        public GetECertisCriterionTask(String ID, EULanguageCodeEnum lang) {
            this.ID = ID;
            this.lang = lang.name().toLowerCase();
        }

        @Override
        public ECertisCriterion call() throws IOException {

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet("https://ec.europa.eu/growth/tools-databases/ecertisrest/criteria/" + ID + "?lang=" + lang);
                httpGet.setHeader("Accept", "application/json");

                ResponseHandler<String> responseHandler = response -> {
                    int status = response.getStatusLine().getStatusCode();

                    if (status == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                };

                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                return mapper.readValue(httpClient.execute(httpGet, responseHandler), ECertisCriterionImpl.class);
            }

        }
    }

    /**
     * Retrieve all the EU criteria IDs from e-Certis service.
     *
     * @return A list with the IDs
     * @throws RetrieverException
     */
    List<String> getAllEUCriteriaID() throws RetrieverException {

        List<String> IDList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(getFromECertis(ALL_CRITERIA));
            JsonNode criteria = root.path("Criterion");

            for (JsonNode criterion : criteria) {
                String tempID = criterion.path("ID").asText();
                IDList.add(tempID);
            }

        } catch (IOException ex) {
            handleMappingException(ex);
        }

        return IDList;
    }

    /**
     * Retrieve all the EU criteria from e-Certis service, alongside
     * with some of their basic info (ID, Name, Description)
     *
     * @throws RetrieverException
     */
    List<SelectableCriterion> getAllEUCriteriaWithBasicInfo() throws RetrieverException {

        List<SelectableCriterion> cList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(getFromECertis(ALL_CRITERIA));
            JsonNode criteria = root.path("Criterion");

            for (JsonNode criterion : criteria) {
                SelectableCriterion sc = new SelectableCriterion();
                sc.setID(criterion.path("ID").asText());
                sc.setName(criterion.path("Name").findValue("value").asText());
                sc.setDescription(criterion.path("Description").findValue("value").asText());
                sc.setSelected(true);
                cList.add(sc);
            }

        } catch (IOException ex) {
            handleMappingException(ex);
        }

        return cList;
    }

    /**
     * Open connection with e - Certis service in order to retrieve data
     *
     * @param url The e-Certis service url
     * @return String representation of retrieved data
     * @throws IOException
     */
    private String getFromECertis(String url) throws IOException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();

                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            };

            return httpClient.execute(httpGet, responseHandler);
        }
    }

    private void handleMappingException(IOException ex) throws RetrieverException {
        String message = ex.getMessage();

        if (ex instanceof JsonParseException) {
            message = ERROR_INVALID_CONTENT;
        }

        if (ex instanceof JsonMappingException) {
            message = ERROR_UNEXPECTED_STRUCTURE;
        }

        logger.log(Level.SEVERE, null, ex);
        throw new RetrieverException(message, ex);
    }

    private SelectableCriterion createSelectableCriterionAsSelected(ECertisCriterion c, boolean asSelected) {
        SelectableCriterion sc = new SelectableCriterion();
        sc.setID(c.getID());
        sc.setName(c.getName());
        sc.setDescription(c.getDescription());
        sc.setSelected(asSelected);
        return sc;
    }

    private SelectableCriterion createSelectableCriterion(ECertisCriterion c) {
        return createSelectableCriterionAsSelected(c, true);
    }

    @Override
    public List<SelectableCriterion> getCriterionList() throws RetrieverException {
        initCriterionMap();
        return criterionMap.values().stream()
                .map(ec -> createSelectableCriterion(ec))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() throws RetrieverException {
        initCriterionMap();
        return criterionMap.values().stream()
                .map(ec -> createSelectableCriterion(ec))
                .collect(Collectors.toMap(sc -> sc.getID(), Function.identity()));
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID) throws RetrieverException {
        initCriterionMap();
        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getLegislationReference()
                : null;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ECERTIS;
    }

}
