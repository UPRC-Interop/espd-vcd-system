package eu.esens.espdvcd.retriever.criteria;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.retriever.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractor implements CriteriaExtractor, CriteriaDataRetriever {

    private List<CriterionType> criterionTypeList;
            
    public enum JurisdictionLevelCodeOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

    public ECertisCriteriaExtractor() {
        
    }
    
    private void getFullListFromeCertis() throws RetrieverException {
        
        // If Not Initialized Yet, Initialize CriterionType List
        if (criterionTypeList == null) {
            criterionTypeList = new ArrayList<>();
                        
            // Multithreading Approach (1)
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Callable<CriterionType>> callables = new HashSet<>();
            
            getAllEuropeanCriteriaIds().forEach(id -> {
                callables.add((Callable<CriterionType>) () -> getCriterion(id));
            });
                       
            try {
                List<Future<CriterionType>> futures = executorService.invokeAll(callables);
                
                for (Future f : futures) {
                    CriterionType c = (CriterionType) f.get();
                    // If Description is not provided, do not add Criterion
                    if (c.getDescription() != null) criterionTypeList.add(c);
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
        
        getFullListFromeCertis();
        List<SelectableCriterion> lc
                = criterionTypeList.stream()
                        .map((CriterionType c) -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                        .collect(Collectors.toList());
        return lc;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) 
            throws RetrieverException {
        
        getFullListFromeCertis();
        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) 
            throws RetrieverException {
        
        getFullListFromeCertis();
        System.out.println("Criterion List Size:" + criterionTypeList.size());
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        Set<SelectableCriterion> fullSet
                = criterionTypeList.stream()
                        .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c, addAsSelected))
                        .collect(Collectors.toSet());
        initialSet.addAll(fullSet);
        System.out.println("Criterion List Size in model:" + initialSet.size());
        return new ArrayList<>(initialSet);
    }

    @Override
    public List<CriterionType> getNationalCriterionMapping(String criterionId, String countryCode)
             throws RetrieverException {
        List<CriterionType> nationalCriterionTypeList = new ArrayList<>();
        
        if (isCountryCodeExists(countryCode)) {
            
            CriterionType source = getCriterion(criterionId);
            JurisdictionLevelCodeOrigin jlco = getCriterionJurisdictionLevelCodeOrigin(source);
            
            switch (jlco) {
                case EUROPEAN:
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(source, countryCode);
                    break;
                case NATIONAL:
                    // Get the EU Parent Criterion
                    CriterionType parent = getParentCriterion(source);
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(parent, countryCode);
                    break;
                case UNKNOWN:
                    throw new RetrieverException("Criterion <<" + criterionId + ">> "
                            + "Cannot be Classified as European or National...");
            }

        } else {
            throw new RetrieverException("Country Code <<" + countryCode + ">> "
                    + "does not exist...");
        }

        return nationalCriterionTypeList;
    }
    
    private ECertisCriterion extractECertisCriterion(CriterionType c) {
        return null;
    }
    
    @Override
    public CriterionType getCriterion(String criterionId) 
            throws RetrieverException {
        CriterionType theCriterion = null;
        BufferedReader br = null;
                
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + criterionId + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setConnectTimeout(15000);
            connection.connect();

            // Http Status 200 = ok
            if (connection.getResponseCode() != 200) {
                throw new RetrieverException("HTTP error code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                builder.append(output);
            }

            StringReader reader = new StringReader(builder.toString());
            theCriterion = JAXB.unmarshal(reader, CriterionType.class);
                   
        } catch (RetrieverException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Criterion with Id <<" + criterionId + ">> does not exist", ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Malformed URL", ex);
        } catch (IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error when trying to connect with e-Certis service", ex);
        } finally {
            // close all streams
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
                    throw new RetrieverException("Error when trying to close reader", e);
                }
            }
        }
                       
        return theCriterion;
    }
    
    public ECertisCriterion getCriterionV2(String criterionId) 
            throws RetrieverException {
        BufferedReader br = null;
        ECertisCriterion c = new ECertisCriterion();
        
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + criterionId + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(15000);
            connection.connect();

            // Http Status 200 = ok
            if (connection.getResponseCode() != 200) {
                throw new RetrieverException("HTTP error code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            
            ObjectMapper mapper = new ObjectMapper();
                        
            // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        
            ECertisCriterion ct = mapper.readValue(builder.toString(), ECertisCriterion.class);
            
            String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ct);
            System.out.println(prettyCt);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Malformed URL", ex);
        } catch (IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error when trying to connect with e-Certis service", ex);
        } finally {
            // close all streams
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
                    throw new RetrieverException("Error when trying to close reader", e);
                }
            }
        }
        
        return c;
    }
    
    @Override
    public List<RequirementGroupType> getEvidences(String criterionId) 
            throws RetrieverException {
        return getCriterion(criterionId).getRequirementGroup();
    }
            
    // Get SubCriterion/s of a European Criterion by Country Code
    private List<CriterionType> getSubCriterion(CriterionType c, String countryCode) {
        return c.getSubCriterion().stream()
                .filter(theCt -> !theCt.getLegislationReference().isEmpty())
                .filter(theCt -> theCt.getLegislationReference().get(0)
                .getJurisdictionLevelCode().getValue().equals(countryCode))
                .collect(Collectors.toList());
    }
    
    // Get Parent Criterion of a National Criterion (using DOM)
    private CriterionType getParentCriterion(CriterionType c) 
            throws RetrieverException {
        CriterionType parent = null;
        String parentId = null;
        final String parentCriterionElem = "ccv:ParentCriterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + c.getID().getValue() + "/");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(parentCriterionElem);

            for (int index = 0; index < nList.getLength(); index++) {
                Node nNode = nList.item(index);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    parentId = eElement.getElementsByTagName(idElem).item(0).getTextContent();
                }
            }
            
            if (parentId == null) throw new RetrieverException("Criterion Id Cannot be found...");
            parent = getCriterion(parentId);
            
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error when trying to get Parent Criterion of <<" + c.getID().getValue() + ">>", ex);
        }
        return parent;
    }
        
    // Get All European Criteria Ids (using DOM)
    public List<String> getAllEuropeanCriteriaIds() 
            throws RetrieverException {
        List<String> europeanCriterionIdList = new ArrayList<>();
        final String criterionElem = "ccv:Criterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA);
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(criterionElem);

            for (int index = 0; index < nList.getLength(); index++) {
                Node nNode = nList.item(index);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    europeanCriterionIdList.add(eElement.getElementsByTagName(idElem).item(0).getTextContent());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error when trying to get All European Criteria Ids", ex);
        }
        return europeanCriterionIdList;
    }
    
    // Extract Given Criterion JurisdictionLevelCode Origin
    private JurisdictionLevelCodeOrigin getCriterionJurisdictionLevelCodeOrigin(CriterionType c) {
            
        JurisdictionLevelCodeOrigin jlco = JurisdictionLevelCodeOrigin.UNKNOWN;
        
        if (!c.getLegislationReference().isEmpty()) {
            // Getting 1st LegislationReference's value in order to evaluate JurisdictionLevelCode Origin
            String jlcValue = c.getLegislationReference().get(0)
                    .getJurisdictionLevelCode()
                    .getValue();

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
    
    public String getJurisdictionLevelCodeFromRequirementGroup(String criterionId) 
            throws RetrieverException {
        BufferedReader br = null;
        String nationality = null;
        
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + criterionId + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(15000);
            connection.connect();

            // Http Status 200 = ok
            if (connection.getResponseCode() != 200) {
                throw new RetrieverException("HTTP error code : " + connection.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            
            ObjectMapper m = new ObjectMapper();
            JsonNode rootNode = m.readTree(builder.toString());
            JsonNode requirementGroupNode = rootNode.path("RequirementGroup");
            
            boolean flag1 = false;
            boolean flag2 = false;
            
            for (JsonNode rqgNode : requirementGroupNode) {
                JsonNode TypeOfEvidenceNode = rqgNode.path("TypeOfEvidence");
                       
                for (JsonNode toeNode : TypeOfEvidenceNode) {
                    JsonNode JurisdictionLevelCodeNode = toeNode.path("JurisdictionLevelCode");
                    
                    for (JsonNode jlcNode : JurisdictionLevelCodeNode) {
                        nationality = jlcNode.asText();
                        flag2 = true;
                    }
                    if (flag2) {
                        flag1 = true;
                        break;
                    }
                }
                if (flag1) break;
            }
                     
        } catch (MalformedURLException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Malformed URL", ex);
        } catch (IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error when trying to connect with e-Certis service", ex);
        } finally {
            // close all streams
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
                    throw new RetrieverException("Error when trying to close reader", e);
                }
            }
        }
        
        return nationality;
    }
    
    // Get All National Entities from e-Certis
//    public List<NationalEntity> getAllNationalEntities() 
//            throws RetrieverException {
//        List<NationalEntity> nationalEntities = new ArrayList<>();
//        BufferedReader br = null;
//        
//        try {
//            URL url = new URL(Constants.ECERTIS_URL + Constants.NATIONAL_ENTITIES);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setConnectTimeout(15000);
//            connection.connect();
//
//            // Http Status 200 = ok
//            if (connection.getResponseCode() != 200) {
//                throw new RetrieverException("HTTP error code : " + connection.getResponseCode());
//            }
//
//            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder builder = new StringBuilder();
//            String output;
//
//            while ((output = br.readLine()) != null) {
//                builder.append(output);
//            }
//            
//            JSONArray data = new JSONArray(builder.toString());
//                        
//            for (int i = 0; i < data.length(); i++) {
//                JSONObject neJson = data.getJSONObject(i);
//                NationalEntity ne = new NationalEntity();
//                ne.setId((String) neJson.get("id"));
//                ne.setName((String) neJson.get("name"));
//                
//                JSONArray regions = neJson.getJSONArray("regions");
//                
//                for (int j = 0; j < regions.length(); j++) {
//                    JSONObject reJson = regions.getJSONObject(j);
//                    String code = reJson.getString("code");
//                    String id = reJson.getString("id");
//                    String name = reJson.getString("name");
//                    
//                    Region region = new Region(code, id, name);
//                    ne.getRegions().add(region);
//                }
//                nationalEntities.add(ne);
//            }
//                        
//        } catch (JSONException ex) {
//            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
//            throw new RetrieverException("JSONException", ex);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
//            throw new RetrieverException("Malformed URL", ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
//            throw new RetrieverException("Error when trying to connect with e-Certis service", ex);
//        } finally {
//            // close all streams
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, e);
//                    throw new RetrieverException("Error when trying to close reader", e);
//                }
//            }
//        }
//        return nationalEntities;
//    }
        
}
