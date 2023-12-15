/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetECertisCriterionRetryingTask;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetFromECertisRetryingTask;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetFromECertisTask;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class ECertisResource implements CriteriaResource, LegislationResource, EvidencesResource {

    private static final Logger LOGGER = Logger.getLogger(ECertisResource.class.getName());

    private static final String ECERTIS_URL = "https://ec.europa.eu/growth/tools-databases/ecertisrest";
    private static final String ALL_CRITERIA_URL = ECERTIS_URL + "/criteria";

    // Contains all European criteria
    private Map<String, ECertisCriterion> criterionMap;
    private Set<String> initialIDSet;

    public ECertisResource() {
        this(null);
    }

    public ECertisResource(List<String> initialIDList) {
        if (initialIDList != null) {
            this.initialIDSet = new HashSet<>(initialIDList);
        }
    }

    /**
     * Apply EU criteria to criterionMap
     *
     * @throws RetrieverException
     */
    private void initCriterionMap() throws RetrieverException {

        if (criterionMap == null) {
            criterionMap = createECertisCriterionMap();
        }
    }

    private List<String> extractCommonIDList(List<String> list1, List<String> list2) {
        List<String> commonIDList = new ArrayList<>(list1);
        commonIDList.retainAll(list2);
        return commonIDList;
    }

    private List<String> extractFullIDList(Set<String> set1, List<String> list2) {
        set1.addAll(list2);
        return new ArrayList<>(set1);
    }

    Map<String, ECertisCriterion> createECertisCriterionMap() throws RetrieverException {

        Map<String, ECertisCriterion> eCertisCriterionMap = new LinkedHashMap<>();

        List<String> fullIDList = initialIDSet != null
                // ? extractCommonIDList(initialIDList, getAllCriteriaID())
                ? extractFullIDList(initialIDSet, getAllCriteriaID())
                : getAllCriteriaID();

        ExecutorService executorService = Executors.newCachedThreadPool();

        Set<GetECertisCriterionRetryingTask> rTasks = new LinkedHashSet<>(fullIDList.size());
        fullIDList.forEach(ID -> rTasks.add(new GetECertisCriterionRetryingTask(ID)));

        try {
            // LOGGER.log(Level.INFO, "Invoke all tasks... START");
            System.out.println("Invoke all tasks... START");
            long startTime = System.currentTimeMillis();
            List<Future<ECertisCriterion>> futures = executorService.invokeAll(rTasks);
            long endTime = System.currentTimeMillis();
            // LOGGER.log(Level.INFO, "Invoke all tasks... FINISH: " + (endTime - startTime) + " ms");
            System.out.println("Invoke all tasks... FINISH: " + (endTime - startTime) + " ms");

            for (Future<ECertisCriterion> f : futures) {

                if (f.isDone()) {

                    try {
                        ECertisCriterion ec = f.get();
                        eCertisCriterionMap.put(ec.getID(), ec);

                    } catch (ExecutionException e) {

                        if (ExceptionUtils.getRootCause(e) instanceof ClientProtocolException) {
                            System.out.println(ExceptionUtils.getRootCauseMessage(e));
                        } else {
                            throw new ExecutionException(e);
                        }
                    }

                }

            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RetrieverException(e);
        }

        return eCertisCriterionMap;
    }

    /**
     * Retrieve all the European criteria IDs from e-Certis service.
     *
     * @return A list with the IDs
     * @throws RetrieverException
     */
    List<String> getAllCriteriaID() throws RetrieverException {

        List<String> criterionIDList = new ArrayList<>();

        try {
            GetFromECertisRetryingTask task = new GetFromECertisRetryingTask(new GetFromECertisTask(ALL_CRITERIA_URL));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(task.call());
            JsonNode criteria = root.path("Criterion");

            for (JsonNode criterion : criteria) {
//                String tempID = criterion.path("ID").asText();
//                criterionIDList.add(tempID);

                // Konstantinos Raptis: After EU e-Certis Update 03-09-2020
                String tempID = criterion.path("ID").path("value").asText();
                criterionIDList.add(tempID);
            }

        } catch (RetryException | ExecutionException | IOException e) {
            throw new RetrieverException(e);
        }

        return criterionIDList;
    }

    /**
     * Retrieve all the EU criteria from e-Certis service, alongside
     * with some of their basic info (ID, Name, Description)
     *
     * @throws RetrieverException
     */
    List<SelectableCriterion> getAllCriteriaBasicInfo() throws RetrieverException {

        List<SelectableCriterion> cList = new ArrayList<>();

        try {
            GetFromECertisRetryingTask task = new GetFromECertisRetryingTask(new GetFromECertisTask(ALL_CRITERIA_URL));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(task.call());
            JsonNode criteria = root.path("Criterion");

            for (JsonNode criterion : criteria) {
                SelectableCriterion sc = new SelectableCriterion();
                sc.setID(criterion.path("ID").asText());
                sc.setName(criterion.path("Name").findValue("value").asText());
                sc.setDescription(criterion.path("Description").findValue("value").asText());
                sc.setSelected(true);
                cList.add(sc);
            }

        } catch (RetryException | ExecutionException | IOException e) {
            throw new RetrieverException(e);
        }

        return cList;
    }

    @Override
    public List<SelectableCriterion> getCriterionList() throws RetrieverException {
        return getCriterionList(EULanguageCodeEnum.EN);
    }

    @Override
    public List<SelectableCriterion> getCriterionList(EULanguageCodeEnum lang) throws RetrieverException {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        initCriterionMap();
        return criterionMap.values().stream()
                .map(ec -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(ec, true))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() throws RetrieverException {
        return getCriterionMap(EULanguageCodeEnum.EN);
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap(EULanguageCodeEnum lang) throws RetrieverException {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        initCriterionMap();
        return criterionMap.values().stream()
                .map(ec -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(ec, true))
                .collect(Collectors.toMap(Criterion::getID, Function.identity()));
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID) throws RetrieverException {
        return getLegislationForCriterion(ID, EULanguageCodeEnum.EN);
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException {

        // failback check
        if (lang != EULanguageCodeEnum.EN) {
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        initCriterionMap();
        return criterionMap.containsKey(ID)
                ? new LegislationReference(criterionMap.get(ID).getLegislationReference())
                : null;
    }

    @Override
    public List<Evidence> getEvidencesForCriterion(String ID) throws RetrieverException {

        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);

        try {
            ECertisCriterion ec = task.call();
            return ModelFactory.ESPD_REQUEST.extractEvidences(ec.getEvidenceGroups());

        } catch (ExecutionException | RetryException | IOException e) {
            throw new RetrieverException(e);
        }
    }

    @Override
    public List<Evidence> getEvidencesForCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException {

        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID, lang);

        try {
            ECertisCriterion ec = task.call();
            return ModelFactory.ESPD_REQUEST.extractEvidences(ec.getEvidenceGroups());

        } catch (ExecutionException | RetryException | IOException e) {
            throw new RetrieverException(e);
        }

    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ECERTIS;
    }

    /**
     * Retrieves an e-Certis Criterion with full data.
     *
     * @param ID The Criterion ID
     * @return The e-Certis Criterion
     * @throws RetrieverException
     */
    public ECertisCriterion getECertisCriterion(String ID) throws RetrieverException {
        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);

        try {
            return task.call();
        } catch (ExecutionException | RetryException | IOException e) {
            throw new RetrieverException(e);
        }
    }

    /**
     * Retrieves an e-Certis Criterion with full data in the specified language.
     *
     * @param ID   The Criterion ID
     * @param lang The Criterion Language (ISO 639-1:2002)
     * @return The e-Certis Criterion
     * @throws RetrieverException
     */
    public ECertisCriterion getECertisCriterion(String ID, EULanguageCodeEnum lang) throws RetrieverException {
        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID, lang);

        try {
            return task.call();
        } catch (ExecutionException | RetryException | IOException e) {
            throw new RetrieverException(e);
        }
    }

    /**
     * Get Parent Criterion of a National Criterion in the specified language.
     *
     * @param ec
     * @param lang
     * @return
     * @throws RetrieverException
     */
    public ECertisCriterion getParentCriterion(ECertisCriterion ec, EULanguageCodeEnum lang) throws RetrieverException {
        if (ec.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to Extract Parent Criterion of " + ec.getID());
        }
        return getECertisCriterion(ec.getParentCriterion().getID(), lang);
    }

    /**
     * Get Parent Criterion of a National Criterion
     *
     * @param ec
     * @return
     * @throws RetrieverException
     */
    public ECertisCriterion getParentCriterion(ECertisCriterion ec) throws RetrieverException {
        if (ec.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to Extract Parent Criterion of " + ec.getID());
        }
        return getECertisCriterion(ec.getParentCriterion().getID());
    }

    /**
     * Get sub-Criteria of a European Criterion by identification code.
     *
     * @param ec   The European Criterion
     * @param code The identification code (ISO 639-1:2002)
     * @return List of subCriteria
     */
    public List<ECertisCriterion> getSubCriterionList(ECertisCriterion ec, String code) {
        return ec.getSubCriteria().stream()
                .filter(c -> c.getLegislationReference() != null)
                .filter(c -> c.getLegislationReference()
                        .getJurisdictionLevelCode().equals(code))
                .collect(Collectors.toList());
    }

}
