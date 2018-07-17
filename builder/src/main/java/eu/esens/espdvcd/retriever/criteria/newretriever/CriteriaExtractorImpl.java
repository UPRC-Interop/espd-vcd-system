package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.CriteriaResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.LegislationResource;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.RequirementGroupResource;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaExtractorImpl implements CriteriaExtractor {

    private List<CriteriaResource> cResourceList;
    private List<LegislationResource> lResourceList;
    private List<RequirementGroupResource> rgResourceList;

    /* package private constructor. Create only through factory */
    CriteriaExtractorImpl(@NotEmpty List<CriteriaResource> cResourceList,
                          @NotEmpty List<LegislationResource> lResourceList,
                          @NotEmpty List<RequirementGroupResource> rgResourceList) {
        this.cResourceList = cResourceList;
        this.lResourceList = lResourceList;
        this.rgResourceList = rgResourceList;
    }

    @Override
    public List<SelectableCriterion> getFullList() {

        Map<String, SelectableCriterion> criterionMap = new LinkedHashMap<>();
        cResourceList.forEach(cResource -> criterionMap.putAll(
                cResource.getCriterionList().stream()
                        .collect(Collectors.toMap(sc -> sc.getID(), Function.identity()))));


//        return cResourceList.getCriterionList().stream()
//                .map(this::addLegislationReference)
//                .map(this::addRequirementGroups)
//                .collect(Collectors.toList());
        return null;
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
        // sc.setLegislationReference(lResource.getLegislationForCriterion(sc.getID()));
        return sc;
    }

    private SelectableCriterion addRequirementGroups(SelectableCriterion sc) {
        // sc.setRequirementGroups(rgResource.getRequirementGroupsForCriterion(sc.getID()));
        return sc;
    }

}
