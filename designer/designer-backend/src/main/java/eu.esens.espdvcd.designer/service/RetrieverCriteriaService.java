package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.ESPDArtefactResource;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;

import java.util.List;

public class RetrieverCriteriaService implements CriteriaService {

    private final CriteriaExtractor predefinedExtractor;
    private int counter = 0;

    public RetrieverCriteriaService(EDMVersion version) {
        CriteriaExtractorBuilder b = new CriteriaExtractorBuilder(version);
        predefinedExtractor = b.build();
    }

    @Override
    public List<SelectableCriterion> getCriteria() throws RetrieverException {
        List<SelectableCriterion> criteria = predefinedExtractor.getFullList();
        counter = 0;
        criteria.forEach(cr -> {
            cr.setUUID(cr.getID());
            idFix(cr.getRequirementGroups());
        });

        return criteria;
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException {
        return predefinedExtractor.getFullList(initialList);
    }

    @Override
    public List<SelectableCriterion> getTranslatedCriteria(String lang) {
        throw new UnsupportedOperationException("Translation is not supported for predefined criteria");
    }

    private void idFix(List<RequirementGroup> reqGroups) {
        counter++;
        for (RequirementGroup reqGroup : reqGroups) {
            reqGroup.setUUID(reqGroup.getID() + "-" + counter);
            idFix(reqGroup.getRequirementGroups());
            List<Requirement> reqs = reqGroup.getRequirements();
            reqs.forEach(req -> req.setUUID(req.getID() + "-" + counter));
        }
    }
}

