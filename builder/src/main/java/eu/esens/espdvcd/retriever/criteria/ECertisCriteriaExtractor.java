package eu.esens.espdvcd.retriever.criteria;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.retriever.utils.Constant;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisSelectableCriterionImpl;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import java.util.LinkedHashSet;
import eu.esens.espdvcd.model.retriever.ECertisSelectableCriterion;

/**
 *
 * @author konstantinos Raptis
 */
public class ECertisCriteriaExtractor implements CriteriaDataRetriever, CriteriaExtractor {

    private List<ECertisSelectableCriterion> criterionList;

    public enum JurisdictionLevelCodeOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

    public ECertisCriteriaExtractor() {

    }

    private void initCriterionList() throws RetrieverException {

        // If Not Initialized Yet, Initialize Criterion List
        if (criterionList == null) {
            criterionList = new ArrayList<>();

            // Multithreading Approach (1)
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Callable<ECertisSelectableCriterion>> callables = new HashSet<>();

            getAllEuropeanCriteriaID().forEach(id -> {
                callables.add((Callable<ECertisSelectableCriterion>) () -> getCriterion(id));
            });

            try {
                List<Future<ECertisSelectableCriterion>> futures = executorService.invokeAll(callables);

                for (Future f : futures) {
                    ECertisSelectableCriterion c = (ECertisSelectableCriterion) f.get();
                    // If Description is not provided, do not add Criterion
                    if (c.getDescription() != null) {
                        criterionList.add(c);
                    }

                }

            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
                throw new RetrieverException(ex);
            }

            executorService.shutdown();

            ////////////////////////////////////////////////////////////////////
            // Sequential Approach (2)
//            for (String ID : getAllEuropeanCriteriaID()) {
//                ECertisSelectableCriterion c = getCriterion(ID);
//                // If Description is not provided, do not add Criterion
//                if (c.getDescription() != null) criterionList.add(c);
//            }
        }
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList() throws RetrieverException {

        initCriterionList();
        List<SelectableCriterion> lc
                = criterionList.stream()
                        .map((ECertisSelectableCriterion c) -> (SelectableCriterion) c)
                        .collect(Collectors.toList());
        return lc;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList)
            throws RetrieverException {

        initCriterionList();
        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList,
            boolean addAsSelected) throws RetrieverException {

        initCriterionList();
        System.out.println("Criterion List Size:" + criterionList.size());
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        Set<SelectableCriterion> fullSet
                = criterionList.stream()
                        .map((ECertisSelectableCriterion c) -> (SelectableCriterion) c)
                        .collect(Collectors.toSet());
        initialSet.addAll(fullSet);
        System.out.println("Criterion List Size in model:" + initialSet.size());
        return new ArrayList<>(initialSet);
    }

    @Override
    public List<ECertisSelectableCriterion> getNationalCriterionMapping(String ID, String countryCode)
            throws RetrieverException {
        List<ECertisSelectableCriterion> nationalCriterionTypeList = new ArrayList<>();

        if (isCountryCodeExist(countryCode)) {

            ECertisSelectableCriterion source = getCriterion(ID);
            JurisdictionLevelCodeOrigin origin = extractCriterionOrigin(source);

            switch (origin) {
                case EUROPEAN:
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(source, countryCode);
                    break;
                case NATIONAL:
                    // Get the EU Parent Criterion
                    ECertisSelectableCriterion parent = getParentCriterion(source);
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(parent, countryCode);
                    break;
                case UNKNOWN:
                    throw new RetrieverException("Error... Criterion <<" + ID 
                            + ">> Cannot be Classified as European or National...");
            }

        } else {
            throw new RetrieverException("Error... Country Code <<" + countryCode 
                    + ">> does not exist...");
        }

        return nationalCriterionTypeList;
    }

    @Override
    public ECertisSelectableCriterion getCriterion(String ID)
            throws RetrieverException {
        
        ECertisSelectableCriterionImpl theCriterion = null;

        try {
            
            String jsonString = getFromECertis(
                    Constant.ECERTIS_URL + Constant.AVAILABLE_EU_CRITERIA + ID + "/", 
                    Constant.ACCEPT_JSON);

            // Pass json string to mapper
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            mapper.setSerializationInclusion(Include.NON_EMPTY);
            theCriterion = mapper.readValue(jsonString, ECertisSelectableCriterionImpl.class);

            // Print JSON String
            // String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(theCriterion);
            // System.out.println(prettyCt);
        } catch (IOException ex) {
            String message = ex.getMessage();

            if (ex instanceof JsonParseException) {
                message = "Error... JSON input contains invalid content";
            }

            if (ex instanceof JsonMappingException) {
                message = "Error... JSON structure does not match structure expected";
            }

            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException(message, ex);
        } 
        
        return theCriterion;
    }

    @Override
    public List<ECertisEvidenceGroup> getEvidences(String ID)
            throws RetrieverException {
        return getCriterion(ID).getEvidenceGroups();
    }

    // Get SubCriterion/s of a European Criterion by Country Code
    private List<ECertisSelectableCriterion> getSubCriterion(ECertisSelectableCriterion c, String countryCode) {
        return c.getSubCriterions().stream()
                .filter(theCt -> theCt.getLegislationReference() != null)
                .filter(theCt -> theCt.getLegislationReference()
                .getJurisdictionLevelCode().equals(countryCode))
                .collect(Collectors.toList());
    }

    // Get Parent Criterion of a National Criterion
    private ECertisSelectableCriterion getParentCriterion(ECertisSelectableCriterion c)
            throws RetrieverException {
        if (c.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to extract parent criterion of " + c.getID());
        }
        return getCriterion(c.getParentCriterion().getID());
    }

    public List<String> getAllEuropeanCriteriaID()
            throws RetrieverException {
        List<String> IDList = new ArrayList<>();
        
        try {
            String jsonString = getFromECertis(
                    Constant.ECERTIS_URL + Constant.AVAILABLE_EU_CRITERIA, 
                    Constant.ACCEPT_JSON);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonString);
            JsonNode criterions = root.path("Criterion");

            for (JsonNode criterion : criterions) {
                String tempID = criterion.path("ID").asText();
                IDList.add(tempID);
            }
        
        } catch (IOException ex) {
            String message = ex.getMessage();

            if (ex instanceof JsonParseException) {
                message = "Error... JSON input contains invalid content";
            }

            if (ex instanceof JsonMappingException) {
                message = "Error... JSON structure does not match structure expected";
            }

            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException(message, ex);
        } 

        return IDList;
    }

    // Extract Given Criterion JurisdictionLevelCode Origin
    private JurisdictionLevelCodeOrigin extractCriterionOrigin(ECertisSelectableCriterion c) {

        JurisdictionLevelCodeOrigin jlco = JurisdictionLevelCodeOrigin.UNKNOWN;

        if (c.getLegislationReference() != null) {

            String jlcValue = c.getLegislationReference()
                    .getJurisdictionLevelCode();

            if (jlcValue.equals("eu")) {
                jlco = JurisdictionLevelCodeOrigin.EUROPEAN;
            } else {
                jlco = JurisdictionLevelCodeOrigin.NATIONAL;
            }
        }
        return jlco;
    }

    // Use Codelists in order to check if given Country Code is Valid
    private boolean isCountryCodeExist(String countryCode) {
        return Codelists.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }
    
    // Open connection with e - Certis service in order to retrieve data
    private String getFromECertis(String url, String accept) throws RetrieverException {
        BufferedReader br = null;
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();
        
        try {
            URL theUrl = new URL(url);            
            
            connection = (HttpURLConnection) theUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", accept);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RetrieverException("Error... HTTP error code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String output;

            // Read stream
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error... Malformed URL address", ex);
        } catch (IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error... Unable to connect with e-Certis service", ex);
        } finally {
            // close all streams
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
                    throw new RetrieverException("Error... Unable to close buffered reader stream", e);
                }
            }
            // disconnect
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return builder.toString();
    }

}
