package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisEvidenceGroup;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.EvidencesResource;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.logging.Logger;

public class CriteriaDataRetrieverImpl implements CriteriaDataRetriever {

    private static final Logger LOGGER = Logger.getLogger(CriteriaDataRetrieverImpl.class.getName());

    private List<EvidencesResource> eResourceList;

    /* package private constructor. Create only through factory */
    CriteriaDataRetrieverImpl(@NotEmpty List<EvidencesResource> eResourceList) {
        // FIXME sort eResourceList maybe has to be done here

        this.eResourceList = eResourceList;
    }

    @Override
    public List<ECertisCriterion> getNationalCriterionMapping(String ID, String countryCode) throws RetrieverException {
        return null;
    }

    @Override
    public ECertisCriterion getCriterion(String ID) throws RetrieverException {
        return null;
    }

    @Override
    public List<ECertisEvidenceGroup> getEvidences(String ID) throws RetrieverException {
        return null;
    }

}
