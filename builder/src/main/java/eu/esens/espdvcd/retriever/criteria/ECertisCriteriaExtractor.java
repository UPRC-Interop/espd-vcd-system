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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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

    // Contains All european Criteria as CriterionType Objects
    private final List<CriterionType> criterionTypeList;
    // Contains All european Criteria as CriterionType Objects with their Ids as Keys
    private final Map<String, CriterionType> criterionTypeMap;
    // Contains All European Criteria Ids 
    private final List<String> criterionTypeIdList;

    public enum JurisdictionLevelCodeOrigin {

        EUROPEAN(), NATIONAL(), UNKNOWN()

    }

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
        // fill criterionType Map
        criterionTypeMap = new HashMap<>();
        criterionTypeList.forEach(ct -> {
            criterionTypeMap.put(ct.getID().getValue(), ct);
        });
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
     * @param sourceId The Source Criterion Id (European or National).
     * @param targetCountryCode The Country Identification according to ISO 3A.
     * @return All National Criteria which mapped with Source Criterion Id.
     */
    @Override
    public List<CriterionType> getNationalCriterionMapping(String sourceId, String targetCountryCode) {
            // throws RetrieverException {
        List<CriterionType> nationalCriteria = new ArrayList<>();

        boolean isCountryCodeExists = isCountryCodeExists(targetCountryCode);
        JurisdictionLevelCodeOrigin jlco = getCriterionJurisdictionLevelCode(sourceId);

        if (isCountryCodeExists) {

            switch (jlco) {
                case EUROPEAN:
                    // approach 1 (maybe its slow)
//                    nationalCriteria = getNationalCriteriaIdsByCountryCode(targetCountryCode)
//                            .parallelStream()
//                            .filter(ncId -> getParentCriterionId(ncId).equals(sourceId))
//                            .map(ncId -> getCriterion(ncId))
//                            .collect(Collectors.toList());
                                        
                    // approach 2 (faster)
                    nationalCriteria = criterionTypeMap.get(sourceId).getSubCriterion().parallelStream()
                            .filter(subCt -> {
                                if (!subCt.getLegislationReference().isEmpty()) {
                                    if (subCt.getLegislationReference().get(0).getJurisdictionLevelCode().getValue().equals(targetCountryCode)) {
                                        return true;
                                    }
                                }
                                return false;
                            })
                            .collect(Collectors.toList());
                    break;
                case NATIONAL:
                    nationalCriteria = getNationalCriteriaIdsByCountryCode(targetCountryCode)
                            .parallelStream()
                            .filter(ncId -> getParentCriterionId(ncId)
                            .equals(getParentCriterionId(sourceId)))
                            .map(ncId -> getCriterion(ncId))
                            .collect(Collectors.toList());
                    break;
                case UNKNOWN:
                    // throw new RetrieverException("Criterion Id <<" + sourceId + ">> does not exist...");
                    System.out.println("Criterion Id <<" + sourceId + ">> does not exist...");
            }

        } else {
            // throw new RetrieverException("Country Code <<" + targetCountryCode + ">> does not exist...");
            System.out.println("Country Code <<\" + targetCountryCode + \">> does not exist...");
        }

        return nationalCriteria;
    }

    /**
     *
     * @param criterionId The Criterion Id (European or National).
     * @return Criterion Data of Criterion with given Id or null if Criterion does not exist.
     */
    @Override
    public CriterionType getCriterion(String criterionId) {
            // throws RetrieverException {
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
                throw new RetrieverException("HTTP error code : " + connection.getResponseCode());
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
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (RetrieverException e) {
            System.err.println("RetrieverException: " + e.getMessage());
        }
        
        // check if criterion exists
        if (ct == null) {
            System.out.println("Criterion with id " + criterionId + " does not exist");
        }
        return ct;
    }

    /**
     *
     * @param criterionId A European Criterion Id
     * @return
     */
    @Override
    public List<RequirementGroupType> getEvidences(String criterionId) {
            // throws RetrieverException {
        List<RequirementGroupType> evidences = new ArrayList<>();

        if (isEuropeanCriterion(criterionId)) {
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
    private String getParentCriterionId(String criterionId) {
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
        
    private JurisdictionLevelCodeOrigin getCriterionJurisdictionLevelCode(String criterionId) {
            // throws RetrieverException {
        JurisdictionLevelCodeOrigin jlco = JurisdictionLevelCodeOrigin.UNKNOWN;
        CriterionType ct = getCriterion(criterionId);

        if (ct != null && !ct.getLegislationReference().isEmpty()) {
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

    private boolean isEuropeanCriterion(String criterionId) {
        return criterionTypeIdList.contains(criterionId);
    }

    private boolean isCountryCodeExists(String countryCode) {
        return Codelists.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }
    
    public String getCountryNameByCountryCode(String countryCode) {
        return Codelists.CountryIdentification
                .getValueForId(countryCode.toUpperCase());
    }
    
}
