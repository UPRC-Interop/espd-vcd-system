package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import java.util.List;
import java.util.stream.Collectors;

public class CriteriaExtractorImpl implements CriteriaExtractor {

    private CriteriaResource cResource;
    private LegislationResource lResource;
    private RequirementGroupResource rgResource;

    private CriteriaExtractorImpl(Builder b) {
        this.cResource = b.cResource;
        this.lResource = b.lResource;
        this.rgResource = b.rgResource;
    }

    @Override
    public List<SelectableCriterion> getFullList() throws RetrieverException {
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

    public static class Builder {

        private CriteriaResource cResource;
        private LegislationResource lResource;
        private RequirementGroupResource rgResource;

        public Builder() {

        }

        public Builder withCriterionResource(CriteriaResource resource) {
            this.cResource = resource;
            return this;
        }

        public Builder withLegislationResource(LegislationResource resource) {
            this.lResource = resource;
            return this;
        }

        public Builder withRequirementGroupResource(RequirementGroupResource resource) {
            this.rgResource = resource;
            return this;
        }

        public CriteriaExtractorImpl build() {
            return new CriteriaExtractorImpl(this);
        }

    }

}
