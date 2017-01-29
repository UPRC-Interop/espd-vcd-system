package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.SelectableCriterion;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
    
    // Contains all european criteria as CriterionType objects
    private final List<CriterionType> criterionTypeList;
    // Contains all european criteria in English lang
    private final List<String> criterionTypeIdList;
    
    public ECertisCriteriaExtractor() {
        criterionTypeIdList = getAllEuropeanCriteriaIds();
        criterionTypeList = criterionTypeIdList
                .parallelStream()
                .map(id -> getCriterion(id))
                // getCriterion will return null if criterion not exists
                // discard that value from our stream
                .filter(ct -> ct != null)
                // if description not provided from e-Certis (null), discard criterion
                .filter(ct -> ct.getDescription() != null)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList() {
        List<SelectableCriterion> lc
                = criterionTypeList.stream()
                        .map((CriterionType c) -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                        .collect(Collectors.toList());
        return lc;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
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
     * @param criterionId A European Criterion Id
     * @param countryCode Country Identification according to ISO 3A 
     * @return All National Criteria
     */
    @Override
    public List<CriterionType> getNationalCriterionMapping(String criterionId, String countryCode) {
        List<CriterionType> nationalCriteria = new ArrayList<>();
        
        boolean isCountryCodeExists = isCountryCodeExists(countryCode);
        boolean isEuCriterionIdExists = isEuCriterionIdExists(criterionId);
        
        if (isCountryCodeExists && isEuCriterionIdExists) {
            // Get National Criteria Ids
            // Use them in order to Get National Criteria Data
            nationalCriteria = getNationalCriteriaIdsByCountryCode(countryCode)
                    .stream()
                    .filter(nationalCriterionId -> getParentCriterionId(nationalCriterionId).equals(criterionId))
                    .map(nationalCriterionId -> getCriterion(nationalCriterionId))
                    .collect(Collectors.toList());
        } else {
            if (!isCountryCodeExists) System.out.println("Country Code " + countryCode + " does not exist...");
            if (!isEuCriterionIdExists) System.out.println("European Criterion Id " + criterionId + " does not exist...");            
        }
        
        return nationalCriteria;
    }
        
    /**
     * 
     * @param criterionId A Criterion Identification Number (Can be a non-European Criterion Id)
     * @return A Specific Criterion Data based on Critirion Id or null if Criterion does not exist
     */
    @Override
    public CriterionType getCriterion(String criterionId) {
        CriterionType ct = null;
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + criterionId + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setConnectTimeout(15000);
            connection.connect();
            
            // Http Status 200 = ok
            if (connection.getResponseCode() != 200) {
                throw new BuilderException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                builder.append(output);
            }

            StringReader reader = new StringReader(builder.toString());
            ct = JAXB.unmarshal(reader, CriterionType.class);
            // close all streams
            br.close();
        } catch (IOException | BuilderException e) {

        }
        // check if criterion exists
        if (ct == null) System.out.println("Criterion with id " + criterionId + " does not exist");
        return ct;
    }

    /**
     * 
     * @param criterionId A European Criterion Id 
     * @return 
     */
    @Override
    public List<RequirementGroupType> getEvidences(String criterionId) {
        List<RequirementGroupType> evidences = new ArrayList<>();
        
        if (isEuCriterionIdExists(criterionId)) {
            List<CriterionType> subCriterions = getCriterion(criterionId).getSubCriterion();
            
            for (CriterionType ct : subCriterions) {
                System.out.println("Criterion name : " + ct.getName().getValue());            
                for (RequirementGroupType rgt : ct.getRequirementGroup()) {
                    System.out.print(rgt.getID().getValue() + " - ");
                }
                System.out.println();
            }
                    
        } else {
            System.out.println("EU criterion " + criterionId + " id does not exist...");            
        }
        
        return evidences;
    }
    
    // Get All National Criteria Ids by Country Code (using DOM)
    private List<String> getNationalCriteriaIdsByCountryCode(String countryCode) {
        List<String> nationalCriteriaIds = new ArrayList<>();
        final String criterionElem = "ccv:Criterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + "?nationalEntity=" + countryCode);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(criterionElem);

            for (int index = 0; index < nList.getLength(); index++) {
                Node nNode = nList.item(index);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    nationalCriteriaIds.add(eElement.getElementsByTagName(idElem).item(0).getTextContent());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {

        }
        return nationalCriteriaIds;
    }
    
    // Get Parent Criterion Id of a National Criterion (using DOM)
    public String getParentCriterionId(String criterionId) {
        String parentCriterionId = null;
        final String parentCriterionElem = "ccv:ParentCriterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_EU_CRITERIA + criterionId + "/");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(parentCriterionElem);

            for (int index = 0; index < nList.getLength(); index++) {
                Node nNode = nList.item(index);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    parentCriterionId = eElement.getElementsByTagName(idElem).item(0).getTextContent();
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {

        }
        return parentCriterionId;
    } 
    
    // Get all eu criteria ids (using DOM)
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
       
    public boolean isEuCriterionIdExists(String criterionId) {
        return criterionTypeIdList.contains(criterionId);   
    }
    
    public boolean isCountryCodeExists(String countryCode) {
        return Codelists.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }
    
}
