package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.utils.Constants;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
public class ECertisCriteriaExtractor implements CriteriaExtractor {

    private final List<CriterionType> criterionTypeList;
    private boolean hasMore = true;
    private int discarded = 0;
    private final int numberOfCriteria;
    
    public ECertisCriteriaExtractor() throws IOException {
        // ---------------------------------------------------------------------
        // Way 1 -> Not-Multithreading
//        numberOfCriteria = 0;
//        criterionTypeList = getCriteriaIDs()
//                .stream()
//                .map(id -> getCriterion(id))
//                .filter(ct -> ct != null)
//                // if description not provided from e-Certis (null)
//                // discard criterion
//                .filter(ct -> ct.getDescription() != null)
//                .collect(Collectors.toList());
        // ---------------------------------------------------------------------
        
        // ---------------------------------------------------------------------
        // Way 2 -> Multithreading with Runnable and ExecutorService
        criterionTypeList = new ArrayList<>();
        
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<String> criteriaIDs = getCriteriaIDs();
        numberOfCriteria = criteriaIDs.size();
        getCriteriaIDs()
                .stream()
                .forEach(cID -> executorService.execute(new GetCriterionTask(cID)));
        
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
     
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList() {
//        while (hasMore) {
//            try {
//                System.out.println("Waiting... unitl all criteria has been loaded");
//                wait();
//            } catch (InterruptedException ex) {
//                System.out.println("InterruptedException: " + ex.getMessage());
//            }
//        }
        
        List<SelectableCriterion> lc
                = criterionTypeList.stream()
                .map((CriterionType c) -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                .collect(Collectors.toList());
        return lc;
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        while (hasMore) {
            try {
                System.out.println("Waiting... unitl all criteria has been loaded");
                wait();
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException: " + ex.getMessage());
            }
        }
        
        return getFullList(initialList, false);
    }

    @Override
    public synchronized List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
        while (hasMore) {
            try {
                System.out.println("Waiting... unitl all criteria has been loaded");
                wait();
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException: " + ex.getMessage());
            }
        }
        
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

    // Get a specific criterion based on critirion id
    private CriterionType getCriterion(String cID) {
        CriterionType ct = null;
        try {
            URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_CRITERIA + cID + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setConnectTimeout(15000);
            connection.connect();
            
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
            
            // if description not provided from e-Certis (null)
            // set criterioType description to "Description not provided from e-Certis"
            //if (ct.getDescription() == null) {
                // approach 1
            //    DescriptionType dt = new DescriptionType();
            //    dt.setValue("Description not provided from e-Certis");
            //    dt.setLanguageID("en");
            //    dt.setLanguageLocaleID(null);
            //    ct.setDescription(dt);
            //}
                       
            // close all streams
            br.close();
            
        } catch (IOException | BuilderException e) {
            
        } 
        return ct;
    }

    // Get all criteria ids (using DOM)
    private List<String> getCriteriaIDs() {
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
    
    private synchronized void addCriterion(CriterionType ct) {
        criterionTypeList.add(ct);
        if (criterionTypeList.size() + discarded == numberOfCriteria) {
            hasMore = false;
            notifyAll();
        }
    }
    
    private synchronized void increaseDiscarded() {
        discarded++;
    }
   
    private class GetCriterionTask implements Runnable {
        
        private String cID;
        
        public GetCriterionTask(String cID) {
            this.cID = cID;
        }
        
        @Override
        public void run() {
            CriterionType ct = null;
            try {
                URL url = new URL(Constants.ECERTIS_URL + Constants.AVAILABLE_CRITERIA + cID + "/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/xml");
                connection.setConnectTimeout(15000);
                connection.connect();
                
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
                br.close();
            } catch (IOException | BuilderException e) {
                
            }
            if (ct != null && ct.getDescription() != null) {
                addCriterion(ct);
            } else {
                increaseDiscarded();
            }
        }
                
    }
    
}
