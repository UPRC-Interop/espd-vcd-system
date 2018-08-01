package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.*;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.ArrayList;
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
    private List<RequirementGroupResource> rgResourceList;

    private static final SchemaVersion DEFAULT_VERSION = SchemaVersion.V2;
    private SchemaVersion version;

    private ESPDArtefactResource artefactResource;
    private CriteriaTaxonomyResource taxonomyResource;
    private ECertisResource eCertisResource;

    public CriteriaExtractorBuilder() {
        this(null);
    }

    public CriteriaExtractorBuilder(SchemaVersion version) {
        cResourceList = new ArrayList<>();
        lResourceList = new ArrayList<>();
        rgResourceList = new ArrayList<>();

        if (version != null) {
            this.version = version;
        } else {
            this.version = DEFAULT_VERSION;
            LOGGER.log(Level.INFO, "Criteria extractor builder initialized with default schema version which is: " + DEFAULT_VERSION);
        }
    }

    public CriteriaExtractorBuilder addCriteriaResource(CriteriaResource resource) {
        cResourceList.add(resource);
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractorBuilder addLegislationResource(LegislationResource resource) {
        lResourceList.add(resource);
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractorBuilder addRequirementGroupsResource(RequirementGroupResource resource) {
        rgResourceList.add(resource);
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractor build() {

        if (cResourceList.isEmpty()) {
            cResourceList.add(createDefaultCriteriaResource());
        }

        if (lResourceList.isEmpty()) {
            lResourceList.addAll(createDefaultLegislationResources());
        }

        if (rgResourceList.isEmpty()) {
            rgResourceList.addAll(createDefaultRequirementGroupResources());
        }

        return new CriteriaExtractorImpl(cResourceList, lResourceList, rgResourceList);
    }

    private CriteriaResource createDefaultCriteriaResource() {
        LOGGER.log(Level.INFO, "Creating default criteria resource");

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
        LOGGER.log(Level.INFO, "Creating default legislation resources");

        initESPDArtefactResource();
        initECertisResource();

        List<LegislationResource> resourceList = new ArrayList<>();
        resourceList.add(artefactResource);
        resourceList.add(eCertisResource);

        return resourceList;
    }

    private List<RequirementGroupResource> createDefaultRequirementGroupResources() {
        LOGGER.log(Level.INFO, "Creating default requirement group resources");

        initCriteriaTaxonomyResource();
        initESPDArtefactResource();

        List<RequirementGroupResource> resourceList = new ArrayList<>();
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
            LOGGER.log(Level.INFO, "Criteria taxonomy Resource initialized");
            taxonomyResource = new CriteriaTaxonomyResource();
        }
    }

    /**
     * Lazy initialization of default eCertis resource
     */
    private void initECertisResource() {

        if (eCertisResource == null) {
            LOGGER.log(Level.INFO, "eCertis Resource initialized");
            eCertisResource = new ECertisResource();
        }
    }

    /**
     * Lazy initialization of default ESPD artefact resource
     */
    private void initESPDArtefactResource() {

        if (artefactResource == null) {
            LOGGER.log(Level.INFO, "ESPD artefact Resource initialized");
            artefactResource = new ESPDArtefactResource(version);
        }
    }

}
