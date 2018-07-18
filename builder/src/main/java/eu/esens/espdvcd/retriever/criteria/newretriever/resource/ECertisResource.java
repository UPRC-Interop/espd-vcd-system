package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class ECertisResource implements CriteriaResource, LegislationResource {

    public static final String ECERTIS_CONFIG_PATH = ECertisCriteriaExtractor.class.getClassLoader()
            .getResource("ecertis.properties").getPath();
    public static final Properties ECERTIS_PROPERTIES = new Properties();

    // Server URL (1st) is the production one (2nd) is the non production one
    public static final String ECERTIS_URL_DEFAULT = "https://ec.europa.eu/growth/tools-databases/ecertisrest/";
    // public static final String ECERTIS_URL_DEFAULT = "https://webgate.acceptance.ec.europa.eu/growth/tools-databases/ecertisrest/";
    private final String ECERTIS_URL;
    // All available eu criteria
    private final String ALL_CRITERIA;

    // Jackson related Errors
    private static final String ERROR_INVALID_CONTENT = "Error... JSON Input Contains Invalid Content";
    private static final String ERROR_UNEXPECTED_STRUCTURE = "Error... JSON Structure does not Match Structure Expected";

    // Contains all eu criteria
    Map<String, ECertisCriterion> criterionMap;

    // Multilinguality vars
    private static final String DEFAULT_LANG = "en";
    private String lang;

    // Multilinguality related errors
    private static final String ERROR_INVALID_LANGUAGE_CODE = "Error... Provided Language Code %s is not Included in codelists";

    // Country code related errors
    private static final String ERROR_INVALID_COUNTRY_CODE = "Error... Provided Country Code %s is not Included in codelists";

    Logger logger = Logger.getLogger(ECertisResource.class.getName());

    public enum CriterionOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

    public ECertisResource() {
        this.lang = DEFAULT_LANG;
        criterionMap = new HashMap<>();

        try {
            ECERTIS_PROPERTIES.load(new FileInputStream(ECERTIS_CONFIG_PATH));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error... Unable to load ecertis configuration file... default value has been used for e-Certis URL instead.", e);
        }
        ECERTIS_URL = ECERTIS_PROPERTIES.getProperty("ecertis_url", ECERTIS_URL_DEFAULT);
        ALL_CRITERIA = ECERTIS_URL + "criteria";

        try {
            applyDataToCriterionMap();
        } catch (RetrieverException ex) {
            logger.log(Level.SEVERE, "Error... e-Certis criterion map initialization FAILED.", ex);
        }
    }

    /**
     * Apply EU criteria to criterionMap
     *
     * @throws RetrieverException
     */
    private void applyDataToCriterionMap() throws RetrieverException {


        ExecutorService executorService = Executors.newCachedThreadPool();
        Set<Callable<ECertisCriterion>> callableSet = new HashSet<>();
        List<String> allEUCriteriaID = getAllEUCriteriaID();
        allEUCriteriaID.forEach(ID -> callableSet.add(() -> getECertisCriterion(ID)));

        try {
            List<Future<ECertisCriterion>> futureList = executorService.invokeAll(callableSet);

            for (Future f : futureList) {
                ECertisCriterion c = (ECertisCriterion) f.get();
                criterionMap.put(c.getID(), c);
            }

        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException(ex);
        }

        executorService.shutdown();

    }

    /**
     * Retrieve the criterion for the given ID.
     *
     * @param ID Criterion ID.
     * @return The criterion or Null if criterion does not exist.
     * @throws RetrieverException
     */
    public ECertisCriterion getECertisCriterion(String ID) throws RetrieverException {

        ECertisCriterion theCriterion = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            theCriterion = mapper.readValue(
                    getFromECertis(ALL_CRITERIA + "/" + ID).toString(),
                    ECertisCriterionImpl.class);
        } catch (IOException ex) {
            handleMappingException(ex);
        }

        return theCriterion;
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
            JsonNode root = mapper.readTree(
                    getFromECertis(ALL_CRITERIA).toString());
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

    private void handleMappingException(IOException ex) throws RetrieverException {
        String message = ex.getMessage();

        if (ex instanceof JsonParseException) {
            message = ERROR_INVALID_CONTENT;
        }

        if (ex instanceof JsonMappingException) {
            message = ERROR_UNEXPECTED_STRUCTURE;
        }

        Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
        throw new RetrieverException(message, ex);
    }

    /**
     * Open connection with e - Certis service in order to retrieve data
     *
     * @param url The e-Certis service url
     * @return String representation of retrieved data
     * @throws RetrieverException
     */
    private StringBuilder getFromECertis(String url) throws RetrieverException {
        BufferedReader br = null;
        HttpURLConnection connection = null;
        StringBuilder response;

        try {
            URL theUrl = new URL(url + "?lang=" + lang);

            connection = (HttpURLConnection) theUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(4 * 1000);    // 4 sec
            connection.setReadTimeout(4 * 1000);       // 4 sec
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RetrieverException("Error... HTTP Error Code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            response = new StringBuilder();
            String inputLine;

            // Read stream
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error... Malformed URL Address", ex);
        } catch (IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error... Unable to Connect with e-Certis Service", ex);
        } finally {
            // close all streams
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
                    throw new RetrieverException("Error... Unable to Close Buffered Reader Stream", e);
                }
            }
            // disconnect
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }

    private SelectableCriterion createSelectableCriterionAsSelected(ECertisCriterion c, boolean asSelected) {
        SelectableCriterion sc = new SelectableCriterion();
        sc.setID(c.getID());
        sc.setName(c.getName());
        sc.setDescription(c.getDescription());
        sc.setSelected(asSelected);
        // sc.setLegislationReference(c.getLegislationReference());
        return sc;
    }

    private SelectableCriterion createSelectableCriterion(ECertisCriterion c) {
        return createSelectableCriterionAsSelected(c, true);
    }

    @Override
    public List<SelectableCriterion> getCriterionList() {
        return criterionMap.values().stream()
                .map(ec -> createSelectableCriterion(ec))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() {
        return null;
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID) {
        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getLegislationReference()
                : null;
    }

}
