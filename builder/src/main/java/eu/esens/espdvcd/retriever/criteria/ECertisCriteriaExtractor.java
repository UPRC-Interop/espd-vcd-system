package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.SelectableCriterion;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXB;
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
public class ECertisCriteriaExtractor implements CriteriaExtractor, CriteriaDataRetriever {

    // Contains All european Criteria as CriterionType Objects
    // !!! Warning : Criteria in which Description is not Provided has been Discarded
    private List<CriterionType> criterionTypeList;
            
    public enum JurisdictionLevelCodeOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

    public ECertisCriteriaExtractor() {
        
    }
    
    private void initCriterionTypeList() 
             throws RetrieverException {
        
        // If Not Initialized Yet, Initialize CriterionType List
        if (criterionTypeList == null) {
            criterionTypeList = new ArrayList<>();
            List<String> ctIds = getAllEuropeanCriteriaIds();
            
            // Synchronized Approach (1)
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Callable<CriterionType>> ctCallableSet = new HashSet<>();
            
            ctIds.forEach(ctId -> {
                ctCallableSet.add((Callable<CriterionType>) () -> getCriterion(ctId));
            });
                        
            try {
                List<Future<CriterionType>> fctList = executorService.invokeAll(ctCallableSet);
                
                for (Future fct : fctList) {
                    CriterionType ct = (CriterionType) fct.get();
                    // If Description is not provided, do not add Criterion
                    if (ct.getDescription() != null) criterionTypeList.add(ct);
                }
                
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
                throw new RetrieverException(ex);
            }
                        
            executorService.shutdown();
            
            ////////////////////////////////////////////////////////////////////
            
            // Sequential Approach (2)
//            for (String ctId : ctIds) {
//                CriterionType ct = getCriterion(ctId);
//                // If Description is not provided, do not add Criterion
//                if (ct.getDescription() != null) criterionTypeList.add(ct);
//            }
        }
    }
        
    @Override
    public synchronized List<SelectableCriterion> getFullList() 
            throws RetrieverException {
        
        initCriterionTypeList();
        List<SelectableCriterion> lc
                = criterionTypeList.stream()
                        .map((CriterionType c) -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                        .collect(Collectors.toList());
        return lc;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) 
            throws RetrieverException {
        
        initCriterionTypeList();
        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) 
            throws RetrieverException {
        
        initCriterionTypeList();
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

    /**
     *
     * @param sourceId The Source Criterion Id (European or National).
     * @param targetCountryCode The Country Identification according to ISO 3A.
     * @return All National Criteria which mapped with Source Criterion Id.
     */
    @Override
    public List<CriterionType> getNationalCriterionMapping(String sourceId, String targetCountryCode)
             throws RetrieverException {
        List<CriterionType> naCts = new ArrayList<>();

        boolean isCountryCodeExists = isCountryCodeExists(targetCountryCode);
        
        if (isCountryCodeExists) {
            
            CriterionType sourceCriterion = getCriterion(sourceId);
            JurisdictionLevelCodeOrigin jlco = getCriterionJurisdictionLevelCodeOrigin(sourceCriterion);
            
            switch (jlco) {
                case EUROPEAN:
                    naCts = extractNational(sourceCriterion, targetCountryCode);
                    break;
                case NATIONAL:
                    CriterionType euCt = getParentCriterion(sourceCriterion);
                    naCts = extractNational(euCt, targetCountryCode);
                    break;
                case UNKNOWN:
                    throw new RetrieverException("Criterions with Id <<" + sourceId 
                            + ">> JurisdictionLevelCode Origin cannot be Classified as EUROPEAN or National...");
            }

        } else {
            throw new RetrieverException("Country Code <<" + targetCountryCode + ">> does not exist...");
        }

        return naCts;
    }
    
    /**
     *
     * @param criterionId The Criterion Id (European or National).
     * @return Criterion Data of Criterion with given Id or null if Criterion does not exist.
     */
    @Override
    public CriterionType getCriterion(String criterionId) 
            throws RetrieverException {
        CriterionType ct = null;
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
            ct = JAXB.unmarshal(reader, CriterionType.class);
                   
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
                       
        return ct;
    }

    /**
     *
     * @param criterionId 
     * @return
     */
    @Override
    public List<RequirementGroupType> getEvidences(String criterionId) 
            throws RetrieverException {
        
        List<RequirementGroupType> evidences = new ArrayList<>();
        
        CriterionType ct = getCriterion(criterionId);
        JurisdictionLevelCodeOrigin jlco = getCriterionJurisdictionLevelCodeOrigin(ct);        
        
        switch (jlco) {
            case EUROPEAN:
                
                break;
            case NATIONAL:
                
                break;
        }
        return evidences;
    }
    
    private List<RequirementGroupType> extractEvidences(CriterionType nationalCriterion) {
        return null;
    }
    
    // Extract All National Criteria of Given European Criterion by Given Country Code
    private List<CriterionType> extractNational(CriterionType euCt, String countryCode) {
        return getSubCriterion(euCt, countryCode);
    }
    
    // Get Parent Criterion of a National Criterion (using DOM)
    private CriterionType getParentCriterion(CriterionType ct) 
            throws RetrieverException {
        CriterionType pc = null;
        String pcId = null;
        final String parentCriterionElem = "ccv:ParentCriterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + ct.getID().getValue() + "/");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(parentCriterionElem);

            for (int index = 0; index < nList.getLength(); index++) {
                Node nNode = nList.item(index);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    pcId = eElement.getElementsByTagName(idElem).item(0).getTextContent();
                }
            }
            
            if (pcId == null) throw new RetrieverException("Criterion Id Cannot be found...");
            pc = getCriterion(pcId);
            
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ECertisCriteriaExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RetrieverException("Error when trying to get Parent Criterion of <<" + ct.getID().getValue() + ">>", ex);
        }
        return pc;
    }
    
    // Get SubCriterion/s of a European Criterion by Country Code
    private List<CriterionType> getSubCriterion(CriterionType ct, String countryCode) {
        return ct.getSubCriterion().stream()
                .filter(theCt -> !theCt.getLegislationReference().isEmpty())
                .filter(theCt -> theCt.getLegislationReference().get(0)
                .getJurisdictionLevelCode().getValue().equals(countryCode))
                .collect(Collectors.toList());
    }
    
    // Get All European Criteria Ids (using DOM)
    private List<String> getAllEuropeanCriteriaIds() {
        List<String> criteriaIDs = new ArrayList<>();
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
                    criteriaIDs.add(eElement.getElementsByTagName(idElem).item(0).getTextContent());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {

        }
        return criteriaIDs;
    }
    
    // Extract Given Criterion JurisdictionLevelCode Origin
    private JurisdictionLevelCodeOrigin getCriterionJurisdictionLevelCodeOrigin(CriterionType ct) {
            
        JurisdictionLevelCodeOrigin jlco = JurisdictionLevelCodeOrigin.UNKNOWN;
        
        if (!ct.getLegislationReference().isEmpty()) {
            // Getting 1st LegislationReference's value in order to evaluate JurisdictionLevelCode Origin
            String jlcValue = ct.getLegislationReference().get(0)
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
       
}
