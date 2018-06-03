package eu.esens.espdvcd.retriever.criteria;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import javax.validation.constraints.NotNull;
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
public class ECertisCriteriaExtractor implements CriteriaDataRetriever, CriteriaExtractor {

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
    private Map<String, ECertisCriterion> eCertisCriterionMap;
    private List<SelectableCriterion> predefinedCriterionList;

    // Multilinguality vars
    private static final String DEFAULT_LANG = "en";
    private String lang;

    // Multilinguality related errors
    private static final String ERROR_INVALID_LANGUAGE_CODE = "Error... Provided Language Code %s is not Included in codelists";

    // Country code related errors
    private static final String ERROR_INVALID_COUNTRY_CODE = "Error... Provided Country Code %s is not Included in codelists";

    private PredefinedESPDCriteriaExtractor predefinedExtractor;

    public enum CriterionOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

    ECertisCriteriaExtractor(@NotNull SchemaVersion version) {
        this.predefinedExtractor = new PredefinedESPDCriteriaExtractor(version);
        this.lang = DEFAULT_LANG;
        try {
            ECERTIS_PROPERTIES.load(new FileInputStream(ECERTIS_CONFIG_PATH));

        } catch (IOException e) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName())
                    .log(Level.SEVERE, "Unable to load ecertis configuration file... default value has been used for e-Certis URL instead", e);
        }
        ECERTIS_URL = ECERTIS_PROPERTIES.getProperty("ecertis_url", ECERTIS_URL_DEFAULT);
        ALL_CRITERIA = ECERTIS_URL + "criteria";
    }

    /**
     * Lazy initialization of European predefined originating criteria
     */
    private void initPredefinedCriterionList() {

        // If Not Initialized Yet, Initialize predefined Criterion List
        if (predefinedCriterionList == null) {
            predefinedCriterionList = predefinedExtractor.getFullList();
        }
    }

    /**
     * Lazy initialization of European e-Certis originating criteria
     *
     * @throws RetrieverException
     */
    private void initECertisCriterionList() throws RetrieverException {

        // If Not Initialized Yet, Initialize e-Certis Criterion List
        if (eCertisCriterionMap == null) {
            eCertisCriterionMap = new LinkedHashMap<>();

            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Callable<ECertisCriterion>> callableSet = new HashSet<>();
            getAllEuropeanCriteriaID().forEach(ID -> callableSet.add(() -> getCriterion(ID)));

            try {
                List<Future<ECertisCriterion>> futureList = executorService.invokeAll(callableSet);

                for (Future f : futureList) {
                    ECertisCriterion c = (ECertisCriterion) f.get();
                    // If Description is not provided, do not add Criterion
                    if (c.getDescription() != null) {
                        eCertisCriterionMap.put(c.getID(), c);
                    }
                }

            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
                throw new RetrieverException(ex);
            }

            executorService.shutdown();
        }
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList() throws RetrieverException {
        initECertisCriterionList();
        initPredefinedCriterionList();

        predefinedCriterionList.forEach(this::applyECertisData);
        return predefinedCriterionList;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) throws RetrieverException {
        initECertisCriterionList();
        initPredefinedCriterionList();

        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) throws RetrieverException {
        initECertisCriterionList();
        initPredefinedCriterionList();

        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        predefinedCriterionList.stream().forEach(sc -> applyECertisDataAsSelected(sc, addAsSelected));
        Set<SelectableCriterion> fullSet = new HashSet<>(predefinedCriterionList);
        initialSet.addAll(fullSet);
        Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, "Criterion List Size in model: " + initialSet.size());
        return new ArrayList<>(initialSet);
    }

    @Override
    public List<ECertisCriterion> getNationalCriterionMapping(String ID, String countryCode)
            throws RetrieverException {
        List<ECertisCriterion> nationalCriterionTypeList = new ArrayList<>();

        if (isCountryCodeExist(countryCode)) {

            ECertisCriterion source = getCriterion(ID);
            CriterionOrigin origin = extractCriterionOrigin(source);

            switch (origin) {
                case EUROPEAN:
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(source, countryCode);
                    break;
                case NATIONAL:
                    // Get the EU Parent Criterion
                    ECertisCriterion parent = getParentCriterion(source);
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(parent, countryCode);
                    break;
                case UNKNOWN:
                    throw new RetrieverException("Error... Criterion " + ID + " cannot be Classified as European or National");
            }

        } else {
            throw new RetrieverException(String.format(ERROR_INVALID_COUNTRY_CODE, countryCode));
        }

        return nationalCriterionTypeList;
    }

    /**
     * Get a specific criterion (European or National) from e-Certis
     *
     * @param ID The criterion ID
     * @return The Criterion
     * @throws RetrieverException
     */
    @Override
    public ECertisCriterion getCriterion(String ID) throws RetrieverException {

        ECertisCriterion theCriterion = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            mapper.setSerializationInclusion(Include.NON_EMPTY);
            theCriterion = mapper.readValue(
                    getFromECertis(ALL_CRITERIA + "/" + ID).toString(),
                    ECertisCriterionImpl.class);
        } catch (IOException ex) {
            handleMappingException(ex);
        }

        return theCriterion;
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

    @Override
    public List<ECertisEvidenceGroup> getEvidences(String ID) throws RetrieverException {
        return getCriterion(ID).getEvidenceGroups();
    }

    /**
     * @param sc The Selectable Criterion, in which the e-Certis data will be applied
     * @return
     */
    private void applyECertisData(final SelectableCriterion sc) {
        applyECertisDataAsSelected(sc, true);
    }

    /**
     * @param sc The Selectable Criterion, in which the e-Certis data will be applied
     * @param sc
     */
    private void applyECertisDataAsSelected(final SelectableCriterion sc, final boolean addAsSelected) {
        // find the equivalent criterion from e-Certis criteria map and (if exist)
        // use it in order to update current predefined criterion
        Optional.ofNullable(eCertisCriterionMap.get(sc.getID()))
                .ifPresent(ec -> {
                    sc.setName(ec.getName());
                    sc.setDescription(ec.getDescription());
                    sc.setLegislationReference(ec.getLegislationReference());
                    sc.setSelected(addAsSelected);
                });
    }

    // Get SubCriterion/s of a European Criterion by Country Code
    private List<ECertisCriterion> getSubCriterion(ECertisCriterion ec, String countryCode) {
        return ec.getSubCriterions().stream()
                .filter(c -> c.getLegislationReference() != null)
                .filter(c -> c.getLegislationReference()
                        .getJurisdictionLevelCode().equals(countryCode))
                .collect(Collectors.toList());
    }

    // Get Parent Criterion of a National Criterion
    private ECertisCriterion getParentCriterion(ECertisCriterion c) throws RetrieverException {
        if (c.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to Extract Parent Criterion of " + c.getID());
        }
        return getCriterion(c.getParentCriterion().getID());
    }

    /**
     * Retrieve the IDs of all European Criteria from e-Certis.
     *
     * @return A list with the IDs
     * @throws RetrieverException
     */
    List<String> getAllEuropeanCriteriaID() throws RetrieverException {

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

    /**
     * Extract Given Criterion Origin (European or National).
     * Criterion Origin will be extracted from Jurisdiction Level Code
     *
     * @param c The criterion
     * @return The Criterion's Origin
     */
    private CriterionOrigin extractCriterionOrigin(ECertisCriterion c) {

        CriterionOrigin origin = CriterionOrigin.UNKNOWN;

        if (c.getLegislationReference() != null) {

            String jlcValue = c.getLegislationReference()
                    .getJurisdictionLevelCode();

            if (jlcValue.equals("eu")) {
                origin = CriterionOrigin.EUROPEAN;
            } else {
                origin = CriterionOrigin.NATIONAL;
            }
        }
        return origin;
    }

    /**
     * Check if given country code exists in the codelists
     *
     * @param countryCode The country code (ISO 3166-1 2A:2006)
     * @return true if exists, false if not
     */
    private boolean isCountryCodeExist(String countryCode) {
        return CodelistsV1.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }

    /**
     * Check if given language code exists in the codelists
     *
     * @param languageCode The European language code
     * @return true if exists, false if not
     */
    private boolean isLanguageCodeExist(String languageCode) {
        return CodelistsV1.LanguageCodeEU
                .containsId(languageCode.toUpperCase());
    }

    /**
     * Set the current language
     *
     * @param lang The European language code
     * @throws RetrieverException In case of an invalid language code
     */
    public void setLang(String lang) throws RetrieverException {
        if (isLanguageCodeExist(lang)) {
            this.lang = lang;
        } else {
            throw new RetrieverException(String.format(ERROR_INVALID_LANGUAGE_CODE, lang));
        }
    }

    /**
     * Initialize language back to default (english).
     */
    public void initLang() {
        lang = DEFAULT_LANG;
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

}
