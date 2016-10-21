/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.utils.Constants;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.interfaces.IECertisService;
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
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import okhttp3.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        numberOfCriteria = 0;
        criterionTypeList = getCriteriaIDs()
                .stream()
                .map(id -> getCriterion(id))
                .filter(ct -> ct != null)
                // if description not provided from e-Certis (null)
                // discard criterion
                .filter(ct -> ct.getDescription() != null)
                .collect(Collectors.toList());
        // ---------------------------------------------------------------------
        
        // ---------------------------------------------------------------------
        // Way 2 -> Multithreading with Runnable and ExecutorService
//        criterionTypeList = new ArrayList<>();
//        
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        List<String> criteriaIDs = getCriteriaIDs();
//        numberOfCriteria = criteriaIDs.size();
//        getCriteriaIDs()
//                .stream()
//                .forEach(cID -> executorService.execute(new GetCriterionTask(cID)));
//        
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(1, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            System.err.println("InterruptedException: " + e.getMessage());
//        }
        // ---------------------------------------------------------------------
        // Way 3 -> Multithreading with Retrofit 2
//        criterionTypeList = new ArrayList<>();
//        List<String> criteriaIDs = getCriteriaIDs();
//        numberOfCriteria = criteriaIDs.size();
//        criteriaIDs
//                .stream()
//                .forEach(cID -> addCriterionR2(cID));
        // ---------------------------------------------------------------------
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
       
//    private void addCriterionR2(String cID) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.ECERTIS_URL)
//                .build();
//        IECertisService eCertisService = retrofit.create(IECertisService.class);
//        Call<ResponseBody> getCriterionCall = eCertisService.getCriterionR2(cID);
//        getCriterionCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    // task available
//                    try {
//                        String criterion = response.body().string();
//                        StringReader reader = new StringReader(criterion);
//                        CriterionType ct = JAXB.unmarshal(reader, CriterionType.class);
//                        if (ct != null && ct.getDescription() != null) {
//                            addCriterion(ct);
//                        } else {
//                            increaseDiscarded();
//                        }
//                    } catch (IOException e) {
//                        System.err.println("IOException: " + e.getMessage());
//                    }
//                } else {
//                    // error response, no access to resource?
//                    System.err.println("error response, no access to resource?");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                
//            }
//        });
//    }
    
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
