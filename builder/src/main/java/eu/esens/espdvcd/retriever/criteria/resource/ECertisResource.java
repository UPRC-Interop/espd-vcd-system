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
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetECertisCriterionRetryingTask;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetFromECertisRetryingTask;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetFromECertisTask;
import eu.esens.espdvcd.retriever.criteria.resource.utils.CriterionUtils;
import eu.esens.espdvcd.retriever.criteria.resource.utils.ECertisURIBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
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
        fullIDList.forEach(ID -> rTasks.add(
                new GetECertisCriterionRetryingTask.Builder(ID)
                        .build()));

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
            GetFromECertisRetryingTask task = new GetFromECertisRetryingTask(
                    new GetFromECertisTask(
                            new ECertisURIBuilder()
                                    .buildCriteriaURI()));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(task.call());
            JsonNode criteria = root.path(ResourceConfig.INSTANCE.getECertisCriterionJsonElement());

            for (JsonNode criterion : criteria) {
                String tempID = criterion
                        .path(ResourceConfig.INSTANCE.getECertisIDJsonElement())
                        .path(ResourceConfig.INSTANCE.getECertisValueJsonElement())
                        .asText();
                criterionIDList.add(tempID);
            }

        } catch (RetryException | ExecutionException | IOException | URISyntaxException e) {
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
            GetFromECertisRetryingTask task = new GetFromECertisRetryingTask(
                    new GetFromECertisTask(new ECertisURIBuilder()
                            .buildCriteriaURI()));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(task.call());
            JsonNode criteria = root.path(ResourceConfig.INSTANCE.getECertisCriterionJsonElement());

            for (JsonNode criterion : criteria) {

                SelectableCriterion sc = new SelectableCriterion();

                sc.setID(criterion
                        .path(ResourceConfig.INSTANCE.getECertisIDJsonElement())
                        .path(ResourceConfig.INSTANCE.getECertisValueJsonElement())
                        .asText());

                sc.setName(criterion
                        .path(ResourceConfig.INSTANCE.getECertisNameJsonElement())
                        .path(ResourceConfig.INSTANCE.getECertisValueJsonElement())
                        .asText());

                sc.setDescription(criterion
                        .path(ResourceConfig.INSTANCE.getECertisDescriptionJsonElement())
                        .path(ResourceConfig.INSTANCE.getECertisValueJsonElement())
                        .asText());

                sc.setSelected(true);
                cList.add(sc);
            }

        } catch (RetryException | ExecutionException | IOException | URISyntaxException e) {
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
    public List<Evidence> getEvidencesForNationalCriterion(@NotNull String ID,
                                                           @Nullable EULanguageCodeEnum lang) throws RetrieverException {

        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask.Builder(ID)
                .lang(ECertisLanguageCodeEnum.valueOf(lang.name()))
                .build();

        try {
            ECertisCriterion ec = task.call();

            if (!CriterionUtils.isEuropean(ec)) { // if it is National
                return ModelFactory.ESPD_REQUEST.extractEvidences(ec.getEvidenceGroups());
            } else { // then it is European
                throw new IllegalArgumentException("Error... Given Criterion id "
                        + ec.getID()
                        + " belongs to a European Criterion.");
            }

        } catch (ExecutionException | RetryException | IOException | IllegalArgumentException e) {
            throw new RetrieverException(e);
        }

    }

    @Override
    public List<Evidence> getEvidencesForEuropeanCriterion(@NotNull String id,
                                                           @Nullable String countryCode,
                                                           @NotNull EULanguageCodeEnum lang) throws RetrieverException {

        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask.Builder(id)
                .lang(ECertisLanguageCodeEnum.valueOf(lang.name()))
                .build();

        try {
            ECertisCriterion ec = task.call();

            if (CriterionUtils.isEuropean(ec)) { // if it is European

                return ModelFactory.ESPD_REQUEST.extractEvidences(
                        getNationalCriteriaByCountryCode(ec, countryCode) // Get National sub-Criteria by country code
                                .stream()
                                .map(ECertisCriterion::getEvidenceGroups) // Get the evidences of that National sub-Criterion
                                .flatMap(List::stream)
                                .collect(Collectors.toList()));

            } else { // then it is National
                throw new IllegalArgumentException("Error... Given Criterion id "
                        + id
                        + " belongs to a National Criterion.");
            }

        } catch (ExecutionException | RetryException | IOException | IllegalArgumentException e) {
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
     * @param id The Criterion id (European or National)
     * @return The e-Certis Criterion
     * @throws RetrieverException
     */
    public ECertisCriterion getECertisCriterion(@NotNull String id) throws RetrieverException {
        return getECertisCriterion(id, ECertisLanguageCodeEnum.EN);
    }

    /**
     * Retrieves an e-Certis Criterion with full data in the specified language.
     *
     * @param id   The Criterion id (European or National)
     * @param lang The language code (ISO 639-1:2002)
     * @return The e-Certis Criterion
     * @throws RetrieverException
     */
    public ECertisCriterion getECertisCriterion(@NotNull String id,
                                                @NotNull ECertisLanguageCodeEnum lang) throws RetrieverException {

        return getECertisCriterion(id, lang, null);
    }

    /**
     * Retrieves an e-Certis Criterion with full data for the specified national entity in the specified language.
     *
     * @param id          The Criterion id (European or National)
     * @param lang        The language code (ISO 639-1:2002)
     * @param countryCode The country code (ISO 2A)
     * @return The e-Certis Criterion
     * @throws RetrieverException
     */
    public ECertisCriterion getECertisCriterion(@NotNull String id,
                                                @NotNull ECertisLanguageCodeEnum lang,
                                                @Nullable ECertisNationalEntityEnum countryCode) throws RetrieverException {

        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask.Builder(id)
                .lang(lang)
                .countryFilter(countryCode)
                .build();

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
    public ECertisCriterion getParentCriterion(ECertisCriterion ec, ECertisLanguageCodeEnum lang) throws RetrieverException {
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
     * Get National Criteria (sub-Criteria) of a European Criterion filtered by country code.
     *
     * @param ec          The Criterion (European)
     * @param countryCode The country code (ISO 2A)
     * @return The National Criteria
     */
    public List<ECertisCriterion> getNationalCriteriaByCountryCode(@NotNull ECertisCriterion ec,
                                                                   @Nullable String countryCode)
            throws RetrieverException, IllegalArgumentException {

        if (CriterionUtils.isEuropean(ec)) { // Check if it is a European criterion

            if (countryCode != null) { // Use filter only if param is not null

                return ec.getSubCriterions().stream()
                        .filter(subCriterion -> subCriterion.getLegislationReference() != null
                                && subCriterion.getLegislationReference().getJurisdictionLevelCode() != null
                                && subCriterion.getLegislationReference().getJurisdictionLevelCode()
                                .equals(countryCode.toLowerCase()))
                        .collect(Collectors.toList());
            } else {

                return ec.getSubCriterions();
            }

        } else {
            throw new IllegalArgumentException("Error... Given Criterion id "
                    + ec.getID()
                    + " belongs to a National Criterion.");
        }
    }

}
