package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.CriteriaResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.LegislationResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.RequirementGroupResource;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaExtractorImpl implements CriteriaExtractor {

    private CriteriaResource cResource;
    private LegislationResource lResource;
    private RequirementGroupResource rgResource;

    /* package private constructor. Create only through factory */
    CriteriaExtractorImpl(@NotNull CriteriaResource cResource,
                          @NotNull LegislationResource lResource,
                          @NotNull RequirementGroupResource rgResource) {
        this.cResource = cResource;
        this.lResource = lResource;
        this.rgResource = rgResource;
    }

    @Override
    public List<SelectableCriterion> getFullList() {
        return cResource.getCriterionList().stream()
                .map(this::addLegislationReference)
                .map(this::addRequirementGroups)
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return null;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
        return null;
    }

    private SelectableCriterion addLegislationReference(SelectableCriterion sc) {
        sc.setLegislationReference(lResource.getLegislationForCriterion(sc.getID()));
        return sc;
    }

    private SelectableCriterion addRequirementGroups(SelectableCriterion sc) {
        sc.setRequirementGroups(rgResource.getRequirementGroupsForCriterion(sc.getID()));
        return sc;
    }

}
