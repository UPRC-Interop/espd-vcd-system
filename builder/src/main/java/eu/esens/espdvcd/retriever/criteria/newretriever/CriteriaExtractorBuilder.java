package eu.esens.espdvcd.retriever.criteria.newretriever;

import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.newretriever.resource.*;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author konstantinos Raptis
 */
public class CriteriaExtractorBuilder {

    private CriteriaResource cResource;
    private LegislationResource lResource;
    private RequirementGroupResource rgResource;

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

        if (version != null) {
            this.version = version;
        } else {
            this.version = DEFAULT_VERSION;
            logger.log(Level.INFO, "Criteria extractor builder initialized with default schema version which is: " + DEFAULT_VERSION);
        }
    }

    public CriteriaExtractorBuilder addCriteriaResource(CriteriaResource resource) {
        this.cResource = resource;
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractorBuilder addLegislationResource(LegislationResource resource) {
        this.lResource = resource;
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractorBuilder addRequirementGroupsResource(RequirementGroupResource resource) {
        this.rgResource = resource;
        return CriteriaExtractorBuilder.this;
    }

    public CriteriaExtractor build() {

        if (cResource == null) {
            initCriteriaTaxonomyResource();
            cResource = taxonomyResource;
        }

        if (lResource == null) {
            initESPDArtefactResource();
            lResource = artefactResource;
        }

        if (rgResource == null) {
            initCriteriaTaxonomyResource();
            rgResource = taxonomyResource;
        }

        return new CriteriaExtractorImpl(cResource, lResource, rgResource);
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
