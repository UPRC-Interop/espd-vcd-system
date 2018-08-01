package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks.GetFromECertisRetryingTask;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks.GetFromECertisTask;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

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
public class ECertisResource implements CriteriaResource, LegislationResource {

    private static final Logger LOGGER = Logger.getLogger(ECertisResource.class.getName());

    private static final String ECERTIS_URL = "https://ec.europa.eu/growth/tools-databases/ecertisrest";
    private static final String ALL_CRITERIA_URL = ECERTIS_URL + "/criteria";

    // Jackson related Errors
    private static final String ERROR_INVALID_CONTENT = "Error... JSON Input Contains Invalid Content";
    private static final String ERROR_UNEXPECTED_STRUCTURE = "Error... JSON Structure does not Match Structure Expected";

    // Contains all eu criteria
    private Map<String, ECertisCriterion> criterionMap;
    private List<String> initialIDList;

    public ECertisResource() {
        this(null);
    }

    public ECertisResource(List<String> initialIDList) {
        this.initialIDList = initialIDList;
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

    Map<String, ECertisCriterion> createECertisCriterionMap() throws RetrieverException {

        Map<String, ECertisCriterion> eCertisCriterionMap = new LinkedHashMap<>();

        List<String> fullIDList = initialIDList != null
                ? extractCommonIDList(initialIDList, getAllEUCriteriaID())
                : getAllEUCriteriaID();

        ExecutorService executorService = Executors.newCachedThreadPool();

        Set<GetFromECertisRetryingTask> rTasks = new LinkedHashSet<>(fullIDList.size());
        fullIDList.forEach(ID -> rTasks.add(new GetFromECertisRetryingTask(
                new GetFromECertisTask(ALL_CRITERIA_URL + "/" + ID))));

        try {
            LOGGER.log(Level.INFO, "Invoke all tasks... START");
            long startTime = System.currentTimeMillis();
            // List<Future<ECertisCriterion>> futureList = executor.invokeAll(tasks);
            List<Future<String>> futures = executorService.invokeAll(rTasks);
            long endTime = System.currentTimeMillis();
            LOGGER.log(Level.INFO, "Invoke all tasks... FINISH: " + (endTime - startTime) + " ms");

            for (Future f : futures) {

                if (f.isDone()) {
                    // ECertisCriterion c = (ECertisCriterion) f.get();
                    // eCertisCriterionMap.put(c.getID(), c);
                    String cString = (String) f.get();

                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                        ECertisCriterion ec = mapper.readValue(cString, ECertisCriterionImpl.class);
                        eCertisCriterionMap.put(ec.getID(), ec);
                    } catch (IOException e) {
                        throw new RetrieverException(e);
                    }
                }

            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RetrieverException(e);
        }

        return eCertisCriterionMap;
    }

    /**
     * Retrieve all the EU criteria IDs from e-Certis service.
     *
     * @return A list with the IDs
     * @throws RetrieverException
     */
    List<String> getAllEUCriteriaID() throws RetrieverException {

        List<String> criterionIDList = new ArrayList<>();

        try {
            GetFromECertisRetryingTask task = new GetFromECertisRetryingTask(new GetFromECertisTask(ALL_CRITERIA_URL));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(task.call());
            JsonNode criteria = root.path("Criterion");

            for (JsonNode criterion : criteria) {
                String tempID = criterion.path("ID").asText();
                criterionIDList.add(tempID);
            }

        } catch (RetryException e) {
            throw new RetrieverException(e);
        } catch (ExecutionException e) {
            throw new RetrieverException(e);
        } catch (IOException ex) {
            handleMappingException(ex);
        }

        return criterionIDList;
    }

    /**
     * Retrieve all the EU criteria from e-Certis service, alongside
     * with some of their basic info (ID, Name, Description)
     *
     * @throws RetrieverException
     */
    List<SelectableCriterion> getAllEUCriteriaWithBasicInfo() throws RetrieverException {

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

        } catch (RetryException e) {
            throw new RetrieverException(e);
        } catch (ExecutionException e) {
            throw new RetrieverException(e);
        } catch (IOException ex) {
            handleMappingException(ex);
        }

        return cList;
    }

    private void handleMappingException(IOException ex) throws RetrieverException {
        String message = ex.getMessage();

        if (ex instanceof JsonParseException) {
            message = ERROR_INVALID_CONTENT;
        }

        if (ex instanceof JsonMappingException) {
            message = ERROR_UNEXPECTED_STRUCTURE;
        }

        LOGGER.log(Level.SEVERE, null, ex);
        throw new RetrieverException(message, ex);
    }

    private SelectableCriterion createSelectableCriterionAsSelected(ECertisCriterion c, boolean asSelected) {
        SelectableCriterion sc = new SelectableCriterion();
        sc.setID(c.getID());
        sc.setName(c.getName());
        sc.setDescription(c.getDescription());
        sc.setSelected(asSelected);
        return sc;
    }

    private SelectableCriterion createSelectableCriterion(ECertisCriterion c) {
        return createSelectableCriterionAsSelected(c, true);
    }

    @Override
    public List<SelectableCriterion> getCriterionList() throws RetrieverException {
        initCriterionMap();
        return criterionMap.values().stream()
                .map(ec -> createSelectableCriterion(ec))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SelectableCriterion> getCriterionMap() throws RetrieverException {
        initCriterionMap();
        return criterionMap.values().stream()
                .map(ec -> createSelectableCriterion(ec))
                .collect(Collectors.toMap(sc -> sc.getID(), Function.identity()));
    }

    @Override
    public LegislationReference getLegislationForCriterion(String ID) throws RetrieverException {
        initCriterionMap();
        return criterionMap.containsKey(ID)
                ? criterionMap.get(ID).getLegislationReference()
                : null;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ECERTIS;
    }

}
