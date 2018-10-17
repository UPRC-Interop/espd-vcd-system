/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.resource.*;
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

    private List<CriteriaResource> cResourceList;
    private List<LegislationResource> lResourceList;
    private List<RequirementsResource> rgResourceList;

    private CriteriaTaxonomyResource taxonomyResource;

    private List<SelectableCriterion> criterionList;

    private EULanguageCodeEnum lang;

    /* package private constructor. Create only through factory */
    CriteriaExtractorImpl(@NotEmpty List<CriteriaResource> cResourceList,
                          @NotEmpty List<LegislationResource> lResourceList,
                          @NotEmpty List<RequirementsResource> rgResourceList) {

        ResourceComparator resourceComparator = new ResourceComparator();
        cResourceList.sort(resourceComparator);
        lResourceList.sort(resourceComparator);
        rgResourceList.sort(resourceComparator);

        this.cResourceList = cResourceList;
        this.lResourceList = lResourceList;
        this.rgResourceList = rgResourceList;
    }

    /**
     * Lazy initialization of criterion list
     */
    private void initCriterionList() {

        if (criterionList == null) {
            criterionList = createCriterionList(cResourceList);
            // add all legislation to that criteria from legislation resources
            criterionList.forEach(this::addLegislationReference);
            // add all requirement groups to that criteria from requirement group resources
            criterionList.forEach(this::addRequirementGroups);
        }
    }

    @Override
    public void setLang(EULanguageCodeEnum lang) {

        // failback check
        if (lang == null || lang != EULanguageCodeEnum.EN) {
            lang = EULanguageCodeEnum.EN;
            LOGGER.log(Level.WARNING, "Warning... European Criteria Multilinguality not supported yet. Language set back to English");
        }

        this.lang = lang;
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
        initCriterionList();
        return criterionList;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return getFullList(initialList, false);
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
        initCriterionList();
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(criterionList);
        initialList.forEach(sc -> sc.setSelected(addAsSelected));
        initialSet.addAll(initialList);
        // apply taxonomy cardinalities before return
        // applyTaxonomyCardinalities(initialList);
        return new ArrayList<>(initialSet);
    }

    private void applyTaxonomyCardinalities(List<SelectableCriterion> criterionList) {

        // lazy initialization of criterion taxonomy resource
        if (taxonomyResource == null) {
            taxonomyResource = new CriteriaTaxonomyResource();
        }

        criterionList.forEach(sc -> taxonomyResource.applyCardinalities(sc));
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
                            .getRequirementsForCriterion(sc.getID()));
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
