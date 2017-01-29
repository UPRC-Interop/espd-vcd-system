package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;

import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementGroupType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class ECertisCriteriaDataRetriever implements CriteriaDataRetriever {
    
    private final List<String> euCriteriaIds;
    
    public ECertisCriteriaDataRetriever() {
        euCriteriaIds = getEuCriteriaIds();
    }
    
    /**
     * 
     * @param euCriterionId
     * @param countryCode
     * @return 
     */
    @Override
    public List<CriterionType> getNationalCriterionMapping(String euCriterionId, String countryCode) {
        List<CriterionType> nationalCriteria = new ArrayList<>();
        
        boolean isCountryCodeExists = isCountryCodeExists(countryCode);
        boolean isEuCriterionIdExists = isEuCriterionIdExists(euCriterionId);
        
        if (isCountryCodeExists && isEuCriterionIdExists) {
            // get national criteria ids
            // use them in order to get national criteria data
            nationalCriteria = getNationalCriteriaIdsByCountryCode(countryCode)
                    .stream()
                    .map(nationalCriterionId -> getCriterion(nationalCriterionId))
                    .collect(Collectors.toList());
        } else {
            if (!isCountryCodeExists) System.out.println("Country code " + countryCode + " does not exist...");
            if (!isEuCriterionIdExists) System.out.println("EU criterion id " + euCriterionId + " does not exist...");            
        }
        
        return nationalCriteria;
    }
        
    /**
     * Get a specific criterion based on critirion id
     * @param criterionId
     * @return 
     */
    @Override
    public CriterionType getCriterion(String criterionId) {
        CriterionType ct = null;
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_CRITERIA + criterionId + "/");
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
     * @param criterionId
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
    
    // Get all national criteria ids by country code (using DOM)
    private List<String> getNationalCriteriaIdsByCountryCode(String countryCode) {
        List<String> nationalCriteriaIds = new ArrayList<>();
        final String criterionElem = "ccv:Criterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_CRITERIA + "?nationalEntity=" + countryCode);
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
    
    // Get all criteria ids (using DOM)
    private List<String> getEuCriteriaIds() {
        List<String> criteriaIDs = new ArrayList<>();
        final String criterionElem = "ccv:Criterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_CRITERIA);
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
        return euCriteriaIds.contains(criterionId);   
    }
    
    public boolean isCountryCodeExists(String countryCode) {
        return Codelists.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }
    
}
