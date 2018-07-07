package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;
import java.util.stream.Collectors;

public class CriteriaExtractorBuilderImpl implements CriteriaExtractorBuilder, CriteriaExtractor {

    private CriteriaResource cResource;
    private LegislationResource lResource;
    private RequirementGroupResource rgResource;

    public CriteriaExtractorBuilderImpl() {

    }

    @Override
    public CriteriaExtractorBuilder withCriteriaResource(CriteriaResource resource) {
        this.cResource = resource;
        return CriteriaExtractorBuilderImpl.this;
    }

    @Override
    public CriteriaExtractorBuilder withLegislationResource(LegislationResource resource) {
        this.lResource = resource;
        return CriteriaExtractorBuilderImpl.this;
    }

    @Override
    public CriteriaExtractorBuilder withRequirementGroupsResource(RequirementGroupResource resource) {
        this.rgResource = resource;
        return CriteriaExtractorBuilderImpl.this;
    }

    @Override
    public CriteriaExtractor build() {
        return CriteriaExtractorBuilderImpl.this;
    }

    @Override
    public List<SelectableCriterion> getFullList() {
        return cResource.getCriterionList().stream()
                .map(sc -> {
                    sc.setLegislationReference(lResource.getLegislationForCriterion(sc.getID()));
                    return sc;
                })
                .map(sc -> {
                    sc.setRequirementGroups(rgResource.getRequirementGroupsForCriterion(sc.getID()));
                    return sc;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) throws RetrieverException {
        return null;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) throws RetrieverException {
        return null;
    }




}
