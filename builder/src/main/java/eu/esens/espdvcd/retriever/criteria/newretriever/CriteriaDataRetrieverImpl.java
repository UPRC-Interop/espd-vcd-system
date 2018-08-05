package eu.esens.espdvcd.retriever.criteria.newretriever;

import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.EvidencesResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks.GetECertisCriterionRetryingTask;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CriteriaDataRetrieverImpl implements CriteriaDataRetriever {

    private static final Logger LOGGER = Logger.getLogger(CriteriaDataRetrieverImpl.class.getName());

    private List<EvidencesResource> eResourceList;

    private enum CriterionOrigin {EUROPEAN, NATIONAL}

    /* package private constructor. Create only through factory */
    CriteriaDataRetrieverImpl(@NotEmpty List<EvidencesResource> eResourceList) {
        this.eResourceList = eResourceList;
    }


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
     * Check if given country code exists in the codelists
     *
     * @param countryCode The country code (ISO 3166-1 2A:2006)
     * @return true if exists, false if not
     */
    private boolean isCountryCodeExist(String countryCode) {
        return CodelistsV2.CountryIdentification
                .containsId(countryCode.toUpperCase());
    }

    // Get SubCriterion/s of a European Criterion by Country Code
    private List<ECertisCriterion> getSubCriterionList(ECertisCriterion ec, String countryCode) {
        return ec.getSubCriterions().stream()
                .filter(c -> c.getLegislationReference() != null)
                .filter(c -> c.getLegislationReference()
                        .getJurisdictionLevelCode().equals(countryCode))
                .collect(Collectors.toList());
    }

    // Get Parent Criterion of a National Criterion
    private ECertisCriterion getParentCriterion(ECertisCriterion ec) throws RetrieverException {
        if (ec.getParentCriterion() == null) {
            throw new RetrieverException("Error... Unable to Extract Parent Criterion of " + ec.getID());
        }
        return getECertisCriterion(ec.getParentCriterion().getID());
    }

    @Override
    public List<SelectableCriterion> getNationalCriterionMapping(String ID, String countryCode) throws RetrieverException {

        List<ECertisCriterion> nationalCriterionTypeList;

        if (isCountryCodeExist(countryCode)) {

            GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);

            try {
                ECertisCriterion source = task.call();
                CriterionOrigin origin = extractCriterionOrigin(source.getLegislationReference());

                switch (origin) {
                    case EUROPEAN:
                        // Extract National Criteria
                        nationalCriterionTypeList = getSubCriterionList(source, countryCode);
                        break;
                    case NATIONAL:
                        // Get the EU Parent Criterion
                        ECertisCriterion parent = getParentCriterion(source);
                        // Extract National Criteria
                        nationalCriterionTypeList = getSubCriterionList(parent, countryCode);
                        break;
                    default:
                        throw new RetrieverException("Error... Criterion " + ID + " cannot be Classified as European or National");
                }

            } catch (ExecutionException | RetryException | IOException e) {
                throw new RetrieverException(e);
            }

        } else {
            throw new RetrieverException(String.format("Error... Provided Country Code %s is not Included in codelists", countryCode));
        }

        return nationalCriterionTypeList.stream()
                .map(ec -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(ec))
                .collect(Collectors.toList());
    }

    private ECertisCriterion getECertisCriterion(String ID) throws RetrieverException {
        GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);

        try {
            return task.call();
        } catch (ExecutionException | RetryException | IOException e) {
            throw new RetrieverException(e);
        }
    }

    @Override
    public SelectableCriterion getCriterion(String ID) throws RetrieverException {
        return ModelFactory.ESPD_REQUEST.extractSelectableCriterion(getECertisCriterion(ID), true);
    }

    @Override
    public List<Evidence> getEvidences(String ID) throws RetrieverException {
        List<Evidence> evidenceList = new ArrayList<>();

        for (EvidencesResource eResource : eResourceList) {
            evidenceList.addAll(eResource.getEvidencesForCriterion(ID));
        }

        return evidenceList;
    }

}
