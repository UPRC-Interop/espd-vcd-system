/**
 * Copyright 2016-2019 University of Piraeus Research Center
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

import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.*;
import eu.esens.espdvcd.retriever.criteria.resource.utils.TaxonomyDataUtils;
import eu.esens.espdvcd.schema.enums.EDMVersion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author konstantinos Raptis
 */
public abstract class CriteriaExtractorBuilder {

    private static final Logger LOGGER = Logger.getLogger(CriteriaExtractorBuilder.class.getName());

    private List<CriteriaResource> cResourceList;
    private List<LegislationResource> lResourceList;
    private List<RequirementsResource> rgResourceList;

    private ContractingOperatorEnum operator = ContractingOperatorEnum.CONTRACTING_ENTITY;
    private EDMVersion version;
    private QualificationApplicationTypeEnum type;

    private ESPDArtefactResource artefactResource;
    private CriteriaTaxonomyResource taxonomyResource;
    private ECertisResource eCertisResource;

    CriteriaExtractorBuilder() {

    }

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
    CriteriaExtractorBuilder(@NotNull EDMVersion version, @NotNull QualificationApplicationTypeEnum type) {
        this.version = version;

        // version 1 does not support self-contained
        if (version == EDMVersion.V1 && type == QualificationApplicationTypeEnum.SELFCONTAINED) {
            LOGGER.log(Level.WARNING, "Warning... Exchange Data Model (EDM) v1 does not " +
                "support self-contained type. Type was set to regulated");
            this.type = QualificationApplicationTypeEnum.REGULATED;
        } else {
            this.type = type;
        }
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
        return this;
    }

    public CriteriaExtractorBuilder withContractingOperator(ContractingOperatorEnum operator) {
        this.operator = operator;
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

        return new CriteriaExtractorImpl(cResourceList, lResourceList, rgResourceList, operator);
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
        // K. Raptis : Remove e-Certis from initialization of the criteria resource v2, as a fix to e-Certis hanging
        initESPDArtefactResource();
//        initECertisResource(taxonomyResource.getCriterionList().stream()
//                .map(Criterion::getID)
//                .collect(Collectors.toList()));

//        taxonomyResource.getCriterionList()
//                .forEach(sc -> {
//                    try {
//                        // apply name and description from e-Certis criteria to taxonomy criteria
//                        applyNameAndDescription(eCertisResource.getCriterionMap().get(sc.getID()), sc);
//                    } catch (RetrieverException e) {
//                        LOGGER.log(Level.SEVERE, e.getMessage());
//                    }
//                });


        taxonomyResource.getCriterionList()
            .forEach(sc -> {
                // apply name and description from ESPD artefact criteria to taxonomy criteria
                applyNameAndDescription(artefactResource.getCriterionMap().get(sc.getID()), sc);
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
        // K. Raptis : Remove e-Certis from initialization of the legislation resource v2, as a fix to e-Certis hanging
//        initECertisResource(artefactResource.getCriterionList().stream()
//            .map(Criterion::getID)
//            .collect(Collectors.toList()));

        List<LegislationResource> resourceList = new ArrayList<>();
//        resourceList.add(eCertisResource);
        resourceList.add(artefactResource);

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

        initCriteriaTaxonomyResource();
        initESPDArtefactResource();

        artefactResource.getCriterionList()
            .forEach(sc -> TaxonomyDataUtils
                .applyTaxonomyData(taxonomyResource.getCriterionMap().get(sc.getID()), sc));

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

//            if (from.getDescription() != null) {
//                to.setDescription(from.getDescription());
//            }
        }
    }

    /**
     * Lazy initialization of default criteria taxonomy resource
     */
    private void initCriteriaTaxonomyResource() {

        if (taxonomyResource == null) {

            switch (type) {

                case REGULATED:
                    taxonomyResource = new RegulatedCriteriaTaxonomyResource();
                    break;

                case SELFCONTAINED:
                    taxonomyResource = new SelfContainedCriteriaTaxonomyResource();
                    break;

                default:
                    LOGGER.log(Level.SEVERE, "Error... Invalid QualificationApplicationType value");

            }

            LOGGER.log(Level.INFO, "Criteria Taxonomy resource initialized");
        }
    }

    /**
     * Lazy initialization of default eCertis resource
     */
//    private void initECertisResource() {
//
//        if (eCertisResource == null) {
//            eCertisResource = new ECertisResource();
//            LOGGER.log(Level.INFO, "eCertis resource initialized with default ID list");
//        }
//    }
    private void initECertisResource(List<String> initialIDList) {

        if (eCertisResource == null) {
            eCertisResource = new ECertisResource(initialIDList);
            LOGGER.log(Level.INFO, "eCertis resource initialized with initial ID list");
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
