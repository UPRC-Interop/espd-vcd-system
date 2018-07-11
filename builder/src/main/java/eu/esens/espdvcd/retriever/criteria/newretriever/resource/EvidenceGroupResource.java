package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;

import java.util.List;

public interface EvidenceGroupResource {

    // FIXME this has to return List<EvidenceGroup>. EvidenceGroup model class has to be created
    List<Evidence> getEvidencesForCriterion(String ID);

}
