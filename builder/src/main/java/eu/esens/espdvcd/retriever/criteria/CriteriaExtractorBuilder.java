/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.*;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author konstantinos Raptis
 */
public class CriteriaExtractorBuilder {

    private static final Logger LOGGER = Logger.getLogger(CriteriaExtractorBuilder.class.getName());

    private List<CriteriaResource> cResourceList;
    private List<LegislationResource> lResourceList;
    private List<RequirementsResource> rgResourceList;

    private EDMVersion version;

    private ESPDArtefactResource artefactResource;
    private CriteriaTaxonomyResource taxonomyResource;
    private ECertisResource eCertisResource;

    /**
     * Criteria extractor builder constructor
     *
     * @param version This parameter will be used from {@link CriteriaExtractorBuilder} if a resource:
     *                ({@link CriteriaResource},
     *                {@link LegislationResource},
     *                {@link RequirementsResource})
     *                left unassigned (null). Resources can be added through relevant
     *                {@link CriteriaExtractorBuilder} methods:
     *                {@link CriteriaExtractorBuilder#addCriteriaResource(CriteriaResource)},
     *                {@link CriteriaExtractorBuilder#addLegislationResource(LegislationResource)},
     *                {@link CriteriaExtractorBuilder#addRequirementsResource(RequirementsResource)}
     */
    public CriteriaExtractorBuilder(@NotNull EDMVersion version) {
        this.version = version;
    }

    /**
     * Lazy initialization of resource lists
     */
    private void initResourceLists() {

        if (cResourceList == null) {
            cResourceList = new ArrayList<>();
        }

        if (lResourceList == null) {
            lResourceList = new ArrayList<>();
        }

        if (rgResourceList == null) {
            rgResourceList = new ArrayList<>();
        }

    }

    public CriteriaExtractorBuilder addCriteriaResource(CriteriaResource resource) {
        initResourceLists();
        cResourceList.add(resource);
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractorBuilder addLegislationResource(LegislationResource resource) {
        initResourceLists();
        lResourceList.add(resource);
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractorBuilder addRequirementsResource(RequirementsResource resource) {
        initResourceLists();
        rgResourceList.add(resource);
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractor build() {
        initResourceLists();

        if (cResourceList.isEmpty()) {
            cResourceList.add(createDefaultCriteriaResource());
        }

        if (lResourceList.isEmpty()) {
            lResourceList.addAll(createDefaultLegislationResources());
        }

        if (rgResourceList.isEmpty()) {
            rgResourceList.addAll(createDefaultRequirementsResources());
        }

        return new CriteriaExtractorImpl(cResourceList, lResourceList, rgResourceList);
    }

    private CriteriaResource createDefaultCriteriaResource() {
        LOGGER.log(Level.INFO, "Creating default criteria resource for Exchange Data Model (EDM) version: " + version);

        switch (version) {

            case V1:
                return createDefaultCriteriaResourceV1();

            case V2:
                return createDefaultCriteriaResourceV2();

            default:
                LOGGER.log(Level.SEVERE, "Unknown Exchange Data Model (EDM) version : " + version);
                return null;

        }
    }

    private CriteriaResource createDefaultCriteriaResourceV1() {

        initESPDArtefactResource();

        return artefactResource;
    }

    private CriteriaResource createDefaultCriteriaResourceV2() {

        initCriteriaTaxonomyResource();
        initECertisResource();

        taxonomyResource.getCriterionList()
                .forEach(sc -> {
                    try {
                        applyNameAndDescription(eCertisResource.getCriterionMap().get(sc.getID()), sc);
                    } catch (RetrieverException e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                });

        return taxonomyResource;
    }

    private List<LegislationResource> createDefaultLegislationResources() {
        LOGGER.log(Level.INFO, "Creating default Legislation resources for Exchange Data Model (EDM) version: " + version);

        switch (version) {

            case V1:
                return createDefaultLegislationResourcesV1();

            case V2:
                return createDefaultLegislationResourcesV2();

            default:
                LOGGER.log(Level.SEVERE, "Unknown Exchange Data Model (EDM) version : " + version);
                return Collections.emptyList();

        }
    }

    private List<LegislationResource> createDefaultLegislationResourcesV1() {

        initESPDArtefactResource();

        List<LegislationResource> resourceList = new ArrayList<>();
        resourceList.add(artefactResource);

        return resourceList;
    }

    private List<LegislationResource> createDefaultLegislationResourcesV2() {

        initESPDArtefactResource();
        initECertisResource();

        List<LegislationResource> resourceList = new ArrayList<>();
        resourceList.add(artefactResource);
        resourceList.add(eCertisResource);

        return resourceList;
    }

    private List<RequirementsResource> createDefaultRequirementsResources() {
        LOGGER.log(Level.INFO, "Creating default Requirements resources for Exchange Data Model (EDM) version: " + version);

        switch (version) {

            case V1:
                return createDefaultRequirementsResourcesV1();

            case V2:
                return createDefaultRequirementsResourcesV2();

            default:
                LOGGER.log(Level.SEVERE, "Unknown Exchange Data Model (EDM) version : " + version);
                return Collections.emptyList();
        }
    }

    private List<RequirementsResource> createDefaultRequirementsResourcesV1() {

        initESPDArtefactResource();

        List<RequirementsResource> resourceList = new ArrayList<>();
        resourceList.add(artefactResource);

        return resourceList;
    }

    private List<RequirementsResource> createDefaultRequirementsResourcesV2() {

        initCriteriaTaxonomyResource();
        initESPDArtefactResource();

        List<RequirementsResource> resourceList = new ArrayList<>();
        resourceList.add(taxonomyResource);
        resourceList.add(artefactResource);

        return resourceList;
    }

    private void applyNameAndDescription(SelectableCriterion from, SelectableCriterion to) {

        if (from != null && to != null) {

            if (from.getName() != null) {
                to.setName(from.getName());
            }

            if (from.getDescription() != null) {
                to.setDescription(from.getDescription());
            }
        }
    }

    /**
     * Lazy initialization of default criteria taxonomy resource
     */
    private void initCriteriaTaxonomyResource() {

        if (taxonomyResource == null) {
            taxonomyResource = new CriteriaTaxonomyResource();
            LOGGER.log(Level.INFO, "Criteria Taxonomy resource initialized");
        }
    }

    /**
     * Lazy initialization of default eCertis resource
     */
    private void initECertisResource() {

        if (eCertisResource == null) {
            eCertisResource = new ECertisResource();
            LOGGER.log(Level.INFO, "eCertis resource initialized");
        }
    }

    /**
     * Lazy initialization of default ESPD artefact resource
     */
    private void initESPDArtefactResource() {

        if (artefactResource == null) {
            artefactResource = new ESPDArtefactResource(version);
            LOGGER.log(Level.INFO, "ESPD Artefact resource initialized for Exchange Data Model (EDM) version: " + version);
        }
    }

}
