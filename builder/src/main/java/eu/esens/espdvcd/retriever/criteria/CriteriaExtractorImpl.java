package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.CriteriaResource;
import eu.esens.espdvcd.retriever.criteria.resource.LegislationResource;
import eu.esens.espdvcd.retriever.criteria.resource.RequirementsResource;
import eu.esens.espdvcd.retriever.criteria.resource.Resource;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaExtractorImpl implements CriteriaExtractor {

    private static final Logger LOGGER = Logger.getLogger(CriteriaExtractorImpl.class.getName());

    // private List<CriteriaResource> cResourceList;
    private List<LegislationResource> lResourceList;
    private List<RequirementsResource> rgResourceList;

    private List<SelectableCriterion> criterionList;

    /* package private constructor. Create only through factory */
    CriteriaExtractorImpl(@NotEmpty List<CriteriaResource> cResourceList,
                          @NotEmpty List<LegislationResource> lResourceList,
                          @NotEmpty List<RequirementsResource> rgResourceList) {

        ResourceComparator resourceComparator = new ResourceComparator();
        lResourceList.sort(resourceComparator);
        rgResourceList.sort(resourceComparator);

        this.criterionList = createCriterionList(cResourceList);
        this.lResourceList = lResourceList;
        this.rgResourceList = rgResourceList;
    }

    private void applyCriterionBasicInfoIfNotNull(SelectableCriterion from, SelectableCriterion to) {

        if (from.getName() != null) {
            to.setName(from.getName());
        }

        if (from.getDescription() != null) {
            to.setDescription(from.getDescription());
        }

        if (from.getTypeCode() != null) {
            to.setTypeCode(from.getTypeCode());
        }

    }

    private List<SelectableCriterion> createCriterionList(List<CriteriaResource> cResourceList) {
        final Map<String, SelectableCriterion> fullCriterionMap = new LinkedHashMap<>();
        cResourceList.sort(new ResourceComparator());

        cResourceList.forEach(cResource -> {
            try {
                cResource.getCriterionList()
                        .forEach(sc -> {

                            if (!fullCriterionMap.containsKey(sc.getID())) {
                                fullCriterionMap.put(sc.getID(), sc);
                            } else {
                                applyCriterionBasicInfoIfNotNull(sc, fullCriterionMap.get(sc.getID()));
                            }

                        });
            } catch (RetrieverException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });

        return fullCriterionMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<SelectableCriterion> getFullList() {
        // add all legislation to that criteria from legislation resources
        criterionList.forEach(this::addLegislationReference);
        // add all requirement groups to that criteria from requirement group resources
        criterionList.forEach(this::addRequirementGroups);

        return criterionList;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return null;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
        return null;
    }

    private void addLegislationReference(SelectableCriterion sc) {

        lResourceList.forEach(lResource -> {
            try {

                if (sc.getLegislationReference() == null) {
                    sc.setLegislationReference(lResource
                            .getLegislationForCriterion(sc.getID()));
                }
            } catch (RetrieverException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });
    }

    private void addRequirementGroups(SelectableCriterion sc) {

        rgResourceList.forEach(rgResource -> {
            try {

                if (sc.getRequirementGroups().isEmpty()) {
                    sc.setRequirementGroups(rgResource
                            .getRequirementGroupsForCriterion(sc.getID()));
                }
            } catch (RetrieverException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });
    }

    private static class ResourceComparator implements Comparator<Resource> {

        @Override
        public int compare(Resource r1, Resource r2) {

            if (r1.getResourceType().getPriority() > r2.getResourceType().getPriority()) {
                return -1;
            }

            if (r1.getResourceType().getPriority() < r2.getResourceType().getPriority()) {
                return 1;
            }

            return 0;
        }

    }

}
