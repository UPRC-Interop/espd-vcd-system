package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.*;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author konstantinos Raptis
 */
public class CriteriaExtractorBuilder {

    private List<CriteriaResource> cResourceList;
    private List<LegislationResource> lResourceList;
    private List<RequirementGroupResource> rgResourceList;

    private CriteriaTaxonomyResource taxonomyResource;
    private ECertisResource eCertisResource;
    private ESPDArtefactResource artefactResource;

    private static final SchemaVersion DEFAULT_VERSION = SchemaVersion.V2;
    private SchemaVersion version;

    Logger logger = Logger.getLogger(CriteriaExtractorBuilder.class.getName());

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
            logger.log(Level.INFO, "Criteria extractor builder initialized with default schema version which is: " + DEFAULT_VERSION);
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
            initCriteriaTaxonomyResource();
            cResourceList.add(taxonomyResource);
        }

        if (lResourceList.isEmpty()) {
            initESPDArtefactResource();
            lResourceList.add(artefactResource);
        }

        if (rgResourceList.isEmpty()) {
            initCriteriaTaxonomyResource();
            rgResourceList.add(taxonomyResource);
        }

        return new CriteriaExtractorImpl(cResourceList, lResourceList, rgResourceList);
    }

    /**
     * Lazy initialization of default criteria taxonomy resource
     */
    private void initCriteriaTaxonomyResource() {

        if (taxonomyResource == null) {
            taxonomyResource = new CriteriaTaxonomyResource();
        }
    }

    /**
     * Lazy initialization of default eCertis resource
     */
    private void initECertisResource() {

        if (eCertisResource == null) {
            eCertisResource = new ECertisResource();
        }
    }

    /**
     * Lazy initialization of default ESPD artefact resource
     */
    private void initESPDArtefactResource() {

        if (artefactResource == null) {
            artefactResource = new ESPDArtefactResource(version);
        }
    }

}
