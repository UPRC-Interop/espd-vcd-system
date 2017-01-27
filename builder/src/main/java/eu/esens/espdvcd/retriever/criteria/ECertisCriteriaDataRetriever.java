package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.utils.Constants;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
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
    
    @Override
    public List<CriterionType> getNationalCriterionMapping(String euCriterionId, String countryCode) {
        List<CriterionType> nationalCriteria = new ArrayList<>();
        if (isCountryCodeExists(countryCode) && isEuCriterionIdExists(euCriterionId)) {
            nationalCriteria = getNationalCriteriaIdsByCountryCode(countryCode)
                    .stream()
                    .map(nationalCriterionId -> getCriterion(nationalCriterionId))
                    .collect(Collectors.toList());
        } else {
            System.out.println("Country code does not exists...");
        }
        return nationalCriteria;
    }

    @Override
    public List<CriterionType> getCriteria(String euCriterionId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RequirementGroup> getEvidences(String euCriterionId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Get a specific criterion based on critirion id
    private CriterionType getCriterion(String criterionId) {
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
        return ct;
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
    
    private boolean isEuCriterionIdExists(String sourceId) {
        return euCriteriaIds.contains(sourceId);   
    }
    
    private boolean isCountryCodeExists(String countryCode) {
        return Codelists.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }
    
}
