package eu.esens.espdvcd.retriever.criteria;

import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.EvidencesResource;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetECertisCriterionRetryingTask;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverImpl implements CriteriaDataRetriever {

    private static final Logger LOGGER = Logger.getLogger(CriteriaDataRetrieverImpl.class.getName());

    private List<EvidencesResource> eResourceList;

    private enum CriterionOrigin {EUROPEAN, NATIONAL}

    /* package private constructor. Create only through factory */
    CriteriaDataRetrieverImpl(@NotEmpty List<EvidencesResource> eResourceList) {
        this.eResourceList = eResourceList;
    }

    /**
     * Specifies if the given legislation reference belongs to
     * a European or National Criterion.
     *
     * @param lr The legislation reference of the Criterion
     * @return
     */
    private CriterionOrigin extractCriterionOrigin(@NotNull LegislationReference lr) {

        if (lr != null && lr.getJurisdictionLevelCode() != null) {

            String jlcValue = lr.getJurisdictionLevelCode();

            if (jlcValue.equals("eu")) {
                return CriterionOrigin.EUROPEAN;
            } else {
                return CriterionOrigin.NATIONAL;
            }

        }

        return null;
    }

    /**
     * Check if the given identification code, exist in the codelists.
     *
     * @param code The identification code (ISO 639-1:2002)
     * @return true if exists, false if not
     */
    private boolean isIdentificationCodeExist(String code) {
        String codeUpperCase = code.toUpperCase();

        return CodelistsV2.LanguageCodeEU
                .containsId(codeUpperCase);
    }

    /**
     * Get sub-Criteria of a European Criterion by identification code.
     *
     * @param ec   The European Criterion
     * @param code The identification code (ISO 639-1:2002)
     * @return List of subCriteria
     */
    private List<ECertisCriterion> getSubCriterionList(ECertisCriterion ec, String code) {
        return ec.getSubCriterions().stream()
                .filter(c -> c.getLegislationReference() != null)
                .filter(c -> c.getLegislationReference()
                        .getJurisdictionLevelCode().equals(code))
                .collect(Collectors.toList());
    }

    /**
     * Get Parent Criterion of a National Criterion
     *
     * @param ec
     * @return
     * @throws RetrieverException
     */
    private ECertisCriterion getParentCriterion(ECertisCriterion ec) throws RetrieverException {
        if (ec.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to Extract Parent Criterion of " + ec.getID());
        }
        return getECertisCriterion(ec.getParentCriterion().getID());
    }

    /**
     * Identifies the origin of given criterion ID (European or National). If
     * the criterion ID found to belong to a European criterion, then method
     * return all its National sub-Criteria, filtered by given country code. If
     * the criterion ID found to belong to a National criterion, then method
     * first searches for the parent European Criterion and then return parent
     * European Criterion National sub-Criteria, filtered again by given country
     * code.
     *
     * @param ID   The source Criterion ID (European or National)
     * @param code The country identification Code (ISO 639-1:2002)
     * @return All National Criteria, which mapped with source Criterion
     * @throws RetrieverException
     */
    @Override
    public List<SelectableCriterion> getNationalCriterionMapping(String ID, String code) throws RetrieverException {

        List<ECertisCriterion> nationalCriterionTypeList;

        if (isIdentificationCodeExist(code)) {

            GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);
            String codeLowerCase = code.toLowerCase();

            try {
                ECertisCriterion source = task.call();
                CriterionOrigin origin = extractCriterionOrigin(source.getLegislationReference());

                switch (origin) {
                    case EUROPEAN:
                        // Extract National Criteria
                        nationalCriterionTypeList = getSubCriterionList(source, codeLowerCase);
                        break;
                    case NATIONAL:
                        // Get the EU Parent Criterion
                        ECertisCriterion parent = getParentCriterion(source);
                        // Extract National Criteria
                        nationalCriterionTypeList = getSubCriterionList(parent, codeLowerCase);
                        break;
                    default:
                        throw new RetrieverException("Error... Criterion " + ID + " cannot be Classified as European or National");
                }

            } catch (ExecutionException | RetryException | IOException e) {
                throw new RetrieverException(e);
            }

        } else {
            throw new RetrieverException(String.format("Error... Provided Country Code %s is not Included in codelists", code));
        }

        return nationalCriterionTypeList.stream()
                .map(ec -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(ec))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an e-Certis Criterion with full data.
     *
     * @param ID The Criterion ID
     * @return The e-Certis Criterion
     * @throws RetrieverException
     */
    ECertisCriterion getECertisCriterion(String ID) throws RetrieverException {
        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);

        try {
            return task.call();
        } catch (ExecutionException | RetryException | IOException e) {
            throw new RetrieverException(e);
        }
    }

    /**
     * Retrieves an e-Certis Criterion, which maps to
     * the related {@link SelectableCriterion} model class.
     *
     * @param ID The Criterion ID (European or National).
     * @return The Criterion
     * @throws RetrieverException
     */
    @Override
    public SelectableCriterion getCriterion(String ID) throws RetrieverException {
        return ModelFactory.ESPD_REQUEST.extractSelectableCriterion(getECertisCriterion(ID), true);
    }

    /**
     * Retrieves all the Evidences of criterion with given ID.
     *
     * @param ID The Criterion ID (European or National).
     * @return The Evidences
     * @throws RetrieverException
     */
    @Override
    public List<Evidence> getEvidences(String ID) throws RetrieverException {
        List<Evidence> evidenceList = new ArrayList<>();

        for (EvidencesResource eResource : eResourceList) {
            evidenceList.addAll(eResource.getEvidencesForCriterion(ID));
        }

        return evidenceList;
    }

}
