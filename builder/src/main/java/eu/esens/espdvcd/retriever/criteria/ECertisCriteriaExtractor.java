package eu.esens.espdvcd.retriever.criteria;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.retriever.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisCriterion;
import eu.esens.espdvcd.model.retriever.interfaces.IECertisEvidenceGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractor implements CriteriaDataRetriever {

    private List<IECertisCriterion> criterionTypeList;
            
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
            Set<Callable<IECertisCriterion>> callables = new HashSet<>();
            
            getAllEuropeanCriteriaIds().forEach(id -> {
                callables.add((Callable<IECertisCriterion>) () -> getCriterion(id));
            });
                       
            try {
                List<Future<IECertisCriterion>> futures = executorService.invokeAll(callables);
                
                for (Future f : futures) {
                    IECertisCriterion c = (IECertisCriterion) f.get();
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
        
//    @Override
//    public synchronized List<SelectableCriterion> getFullList() throws RetrieverException {
//        
//        getFullListFromeCertis();
//        List<SelectableCriterion> lc
//                = criterionTypeList.stream()
//                        .map((CriterionType c) -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
//                        .collect(Collectors.toList());
//        return lc;
//    }
//
//    @Override
//    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) 
//            throws RetrieverException {
//        
//        getFullListFromeCertis();
//        return getFullList(initialList, false);
//    }
//
//    @Override
//    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) 
//            throws RetrieverException {
//        
//        getFullListFromeCertis();
//        System.out.println("Criterion List Size:" + criterionTypeList.size());
//        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
//        initialSet.addAll(initialList);
//        Set<SelectableCriterion> fullSet
//                = criterionTypeList.stream()
//                        .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c, addAsSelected))
//                        .collect(Collectors.toSet());
//        initialSet.addAll(fullSet);
//        System.out.println("Criterion List Size in model:" + initialSet.size());
//        return new ArrayList<>(initialSet);
//    }

    @Override
    public List<IECertisCriterion> getNationalCriterionMapping(String criterionId, String countryCode)
             throws RetrieverException {
        List<IECertisCriterion> nationalCriterionTypeList = new ArrayList<>();
        
        if (isCountryCodeExists(countryCode)) {
            
            IECertisCriterion source = getCriterion(criterionId);
            JurisdictionLevelCodeOrigin jlco = getCriterionJurisdictionLevelCodeOrigin(source);
            
            switch (jlco) {
                case EUROPEAN:
                    // Extract National Criteria
                    nationalCriterionTypeList = getSubCriterion(source, countryCode);
                    break;
                case NATIONAL:
                    // Get the EU Parent Criterion
                    IECertisCriterion parent = getParentCriterion(source);
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
    public IECertisCriterion getCriterion(String criterionId)
            throws RetrieverException {
        BufferedReader br = null;
        ECertisCriterion theCriterion = null;
        
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

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;
            
            // Read stream
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
                       
            // Pass json string to mapper
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            theCriterion = mapper.readValue(builder.toString(), ECertisCriterion.class);
                        
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
    public List<IECertisEvidenceGroup> getEvidences(String criterionId) 
            throws RetrieverException {
        return getCriterion(criterionId).getEvidenceGroup();
    }
            
    // Get SubCriterion/s of a European Criterion by Country Code
    private List<IECertisCriterion> getSubCriterion(IECertisCriterion c, String countryCode) {
        return c.getSubCriterion().stream()
                .filter(theCt -> !theCt.getLegislationReference().isEmpty())
                .filter(theCt -> theCt.getLegislationReference().get(0)
                .getJurisdictionLevelCode().equals(countryCode))
                .collect(Collectors.toList());
    }
    
    // Get Parent Criterion of a National Criterion
    private IECertisCriterion getParentCriterion(IECertisCriterion c) 
            throws RetrieverException {
        if (c.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to extract parent criterion of " + c.getID());
        }
        return getCriterion(c.getParentCriterion().getID());
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
    private JurisdictionLevelCodeOrigin getCriterionJurisdictionLevelCodeOrigin(IECertisCriterion c) {
            
        JurisdictionLevelCodeOrigin jlco = JurisdictionLevelCodeOrigin.UNKNOWN;
        
        if (!c.getLegislationReference().isEmpty()) {
            // Getting 1st LegislationReference's value in order to evaluate JurisdictionLevelCode Origin
            String jlcValue = c.getLegislationReference().get(0)
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
