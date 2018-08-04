package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.codelist.enums.ConfidentialityLevelEnum;
import eu.esens.espdvcd.model.EvidenceIssuerDetails;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisEvidence;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceIssuerParty;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks.GetECertisCriterionRetryingTask;
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
public class ECertisResource implements CriteriaResource, LegislationResource, EvidencesResource {

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

            for (Future f : futures) {

                if (f.isDone()) {
                    ECertisCriterion ec = (ECertisCriterion) f.get();
                    eCertisCriterionMap.put(ec.getID(), ec);
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

    /**
     * Create a new selectable criterion from an e-Certis criterion.
     *
     * @param ec         The e-Certis criterion
     * @param asSelected
     * @return
     */
    private SelectableCriterion createSelectableCriterionAsSelected(ECertisCriterion ec, boolean asSelected) {
        SelectableCriterion sc = new SelectableCriterion();
        sc.setID(ec.getID());
        sc.setName(ec.getName());
        sc.setDescription(ec.getDescription());
        sc.setSelected(asSelected);
        sc.setLegislationReference(ec.getLegislationReference());
        return sc;
    }

    /**
     * Create a new selectable criterion from an e-Certis criterion.
     *
     * @param ec The e-Certis criterion
     * @return
     */
    private SelectableCriterion createSelectableCriterion(ECertisCriterion ec) {
        return createSelectableCriterionAsSelected(ec, true);
    }

    private Evidence extractEvidence(ECertisEvidence evidence) {
        Evidence e = new Evidence();
        e.setID(evidence.getID());
        e.setDescription(evidence.getDescription());
        e.setConfidentialityLevelCode(ConfidentialityLevelEnum.PUBLIC.name());

        if (!evidence.getEvidenceDocumentReference().isEmpty()
                && evidence.getEvidenceDocumentReference().get(0).getAttachment() != null
                && evidence.getEvidenceDocumentReference().get(0).getAttachment().getExternalReference() != null
                && evidence.getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI() != null) {

            e.setEvidenceURL(evidence.getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI());
        }

        if (!evidence.getEvidenceIssuerParty().isEmpty()) {
            e.setEvidenceIssuer(createEvidenceIssuerDetails(evidence.getEvidenceIssuerParty().get(0)));
        }

        return e;
    }

    private EvidenceIssuerDetails createEvidenceIssuerDetails(ECertisEvidenceIssuerParty evidenceIssuerParty) {
        EvidenceIssuerDetails issuerDetails = new EvidenceIssuerDetails();

        if (!evidenceIssuerParty.getPartyName().isEmpty()
                && evidenceIssuerParty.getPartyName().get(0).getName() != null) {

            issuerDetails.setName(evidenceIssuerParty.getPartyName().get(0).getName());
        }

        issuerDetails.setWebsite(evidenceIssuerParty.getWebsiteURI());

        return issuerDetails;
    }

    private List<Evidence> extractEvidences(ECertisEvidenceGroup eg) {
        return eg.getEvidences()
                .stream()
                .map(e -> extractEvidence(e))
                .collect(Collectors.toList());
    }

    private List<Evidence> extractEvidences(List<ECertisEvidenceGroup> egList) {
        List<Evidence> evidenceList = new ArrayList<>();

        for (ECertisEvidenceGroup eg : egList) {
            evidenceList.addAll(extractEvidences(eg));
        }

        return evidenceList;
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
    public List<Evidence> getEvidencesForCriterion(String ID) throws RetrieverException {
        initCriterionMap();

        if (criterionMap.containsKey(ID)) {
            return extractEvidences(criterionMap.get(ID).getEvidenceGroups());
        }

        return Collections.emptyList();
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ECERTIS;
    }

}
