package eu.esens.espdvcd.retriever.criteria;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.retriever.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
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
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import java.util.LinkedHashSet;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractor implements CriteriaDataRetriever, CriteriaExtractor {

    private List<ECertisCriterion> criterionTypeList;
            
    public enum JurisdictionLevelCodeOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

    public ECertisCriteriaExtractor() {
        
    }
    
    private void getFullListFromECertis() throws RetrieverException {
        
        // If Not Initialized Yet, Initialize CriterionType List
        if (criterionTypeList == null) {
            criterionTypeList = new ArrayList<>();
                        
            // Multithreading Approach (1)
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Callable<ECertisCriterion>> callables = new HashSet<>();
            
            getAllEuropeanCriteriaID().forEach(id -> {
                callables.add((Callable<ECertisCriterion>) () -> getCriterion(id));
            });
                       
            try {
                List<Future<ECertisCriterion>> futures = executorService.invokeAll(callables);
                
                for (Future f : futures) {
                    ECertisCriterion c = (ECertisCriterion) f.get();
                    // If Description is not provided, do not add Criterion
                    if (c.getDescription() != null) 
                        criterionTypeList.add(c);
                    
                }
                
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
                throw new RetrieverException(ex);
            }
                        
            executorService.shutdown();
            
            ////////////////////////////////////////////////////////////////////
            
            // Sequential Approach (2)
//            for (String id : getAllEuropeanCriteriaIds()) {
//                CriterionType c = getCriterion(id);
//                // If Description is not provided, do not add Criterion
//                if (c.getDescription() != null) criterionTypeList.add(c);
//            }
        }
    }
        
    @Override
    public synchronized List<SelectableCriterion> getFullList() throws RetrieverException {
        
        getFullListFromECertis();
        List<SelectableCriterion> lc
                = criterionTypeList.stream()
                        .map((ECertisCriterion c) -> {
                            // convertion from ECertisCriterion to SelectableCriterion needed
                            return new SelectableCriterion();
                        })
                        .collect(Collectors.toList());
        return lc;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) 
            throws RetrieverException {
        
        getFullListFromECertis();
        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) 
            throws RetrieverException {
        
        getFullListFromECertis();
        System.out.println("Criterion List Size:" + criterionTypeList.size());
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        Set<SelectableCriterion> fullSet
                = criterionTypeList.stream()
                        .map((ECertisCriterion c) -> {
                            // convertion from ECertisCriterion to SelectableCriterion needed
                            return new SelectableCriterion();
                        })
                        .collect(Collectors.toSet());
        initialSet.addAll(fullSet);
        System.out.println("Criterion List Size in model:" + initialSet.size());
        return new ArrayList<>(initialSet);
    }

    @Override
    public List<ECertisCriterion> getNationalCriterionMapping(String criterionId, String countryCode)
             throws RetrieverException {
        List<ECertisCriterion> nationalCriterionTypeList = new ArrayList<>();
        
        if (isCountryCodeExists(countryCode)) {
            
            ECertisCriterion source = getCriterion(criterionId);
            JurisdictionLevelCodeOrigin jlco = getCriterionJurisdictionLevelCodeOrigin(source);
            
            switch (jlco) {
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
                    throw new RetrieverException("Error... Criterion <<" + criterionId + ">> "
                            + "Cannot be Classified as European or National...");
            }

        } else {
            throw new RetrieverException("Error... Country Code <<" + countryCode + ">> "
                    + "does not exist...");
        }

        return nationalCriterionTypeList;
    }
    
    @Override
    public ECertisCriterion getCriterion(String criterionId)
            throws RetrieverException {
        BufferedReader br = null;
        ECertisCriterionImpl theCriterion = null;
        
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + criterionId + "/");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RetrieverException("Error... HTTP error code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String output;
            
            // Read stream
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
                       
            // Pass json string to mapper
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            mapper.setSerializationInclusion(Include.NON_EMPTY);
            theCriterion = mapper.readValue(builder.toString(), ECertisCriterionImpl.class);
                        
            // Print JSON String
            // String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(theCriterion);
            // System.out.println(prettyCt);
                   
        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error... Malformed URL address", ex);
        } catch (IOException ex) {
            String message = "Error... Unable to connect with e-Certis service";
            
            if (ex instanceof JsonParseException) {
                message = "Error... JSON input contains invalid content";
            } 
            
            if (ex instanceof JsonMappingException) {
                message = "Error... JSON structure does not match structure expected";
            } 
            
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException(message, ex);
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
        }
        
        return theCriterion;
    }
       
    @Override
    public List<ECertisEvidenceGroup> getEvidences(String criterionId) 
            throws RetrieverException {
        return getCriterion(criterionId).getEvidenceGroup();
    }
            
    // Get SubCriterion/s of a European Criterion by Country Code
    private List<ECertisCriterion> getSubCriterion(ECertisCriterion c, String countryCode) {
        return c.getSubCriterion().stream()
                .filter(theCt -> theCt.getLegislationReference() != null)
                .filter(theCt -> theCt.getLegislationReference()
                .getJurisdictionLevelCode().equals(countryCode))
                .collect(Collectors.toList());
    }
    
    // Get Parent Criterion of a National Criterion
    private ECertisCriterion getParentCriterion(ECertisCriterion c) 
            throws RetrieverException {
        if (c.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to extract parent criterion of " + c.getID());
        }
        return getCriterion(c.getParentCriterion().getID());
    }
                
    public List<String> getAllEuropeanCriteriaID() 
            throws RetrieverException {
        List<String> IDList = new ArrayList<>();
        BufferedReader br = null;
                
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RetrieverException("Error... HTTP error code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String output;
            
            // Read stream
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
                
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(builder.toString());
            JsonNode criterions = root.path("Criterion");
            
            for (JsonNode criterion : criterions) {
                String tempID = criterion.path("ID").asText();
                IDList.add(tempID);
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error... Malformed URL address", ex);
        } catch (IOException ex) {
            String message = "Error... Unable to connect with e-Certis service";
            
            if (ex instanceof JsonParseException) {
                message = "Error... JSON input contains invalid content";
            } 
            
            if (ex instanceof JsonMappingException) {
                message = "Error... JSON structure does not match structure expected";
            } 
            
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException(message, ex);
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
        }
        
        return IDList;        
    }
    
    // Extract Given Criterion JurisdictionLevelCode Origin
    private JurisdictionLevelCodeOrigin getCriterionJurisdictionLevelCodeOrigin(ECertisCriterion c) {
            
        JurisdictionLevelCodeOrigin jlco = JurisdictionLevelCodeOrigin.UNKNOWN;
        
        if (c.getLegislationReference() != null ) {
            
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
    
    // User Codelists in order to check if given Country Code is Valid
    private boolean isCountryCodeExists(String countryCode) {
        return Codelists.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }
        
}
