/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.SelectableCriterion;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
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
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractor implements CriteriaExtractor {

    private final List<CriterionType> criterionTypeList;
    private static final String ECERTIS_BASE_URL = "https://ec.europa.eu/growth/tools-databases/ecertisrest/";
    private static final String ECERTIS_AVAILABLE_CRITERIA = "criteria/";

    public ECertisCriteriaExtractor() throws IOException {
        criterionTypeList = getCritiriaIDs()
                .stream()
                .map(id -> getCritirion(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectableCriterion> getFullList() {
        List<SelectableCriterion> lc
                = criterionTypeList.stream()
                .map((CriterionType c) -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                .collect(Collectors.toList());
        return lc;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return getFullList(initialList, false);
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
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

    // get a specific criterion based on critirion id
    // TO DO. see how to avoid null in return type
    private CriterionType getCritirion(String cID) {
        try {
            URL url = new URL(ECERTIS_BASE_URL + ECERTIS_AVAILABLE_CRITERIA + cID + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                builder.append(output);
            }

            StringReader reader = new StringReader(builder.toString());
            CriterionType ct = JAXB.unmarshal(reader, CriterionType.class);
                        
            return ct;

        } catch (IOException e) {

        }
        return null;
    }

    // get all criteria ids (using DOM)
    // TO DO. Do something when list is empty !!!
    private List<String> getCritiriaIDs() {
        List<String> criteriaIDs = new ArrayList<>();
        final String criterionElem = "ccv:Criterion";
        final String idElem = "cbc:ID";

        try {
            URL url = new URL(ECERTIS_BASE_URL + ECERTIS_AVAILABLE_CRITERIA);
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

        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {

        }
        return criteriaIDs;
    }

}
