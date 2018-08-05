package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.EvidencesResource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverBuilder {

    private static final Logger LOGGER = Logger.getLogger(CriteriaDataRetrieverBuilder.class.getName());

    private List<EvidencesResource> eResourceList;
    private ECertisResource eCertisResource;

    public CriteriaDataRetrieverBuilder() {
        eResourceList = new ArrayList<>();
    }

    public CriteriaDataRetrieverBuilder addEvidencesResource(EvidencesResource resource) {
        eResourceList.add(resource);
        return CriteriaDataRetrieverBuilder.this;
    }

    public CriteriaDataRetriever build() {

        if (eResourceList.isEmpty()) {
            eResourceList.addAll(createDefaultEvidencesResource());
        }

        return new CriteriaDataRetrieverImpl(eResourceList);
    }

    private List<EvidencesResource> createDefaultEvidencesResource() {
        LOGGER.log(Level.INFO, "Creating default evidences resource");

        initECertisResource();

        List<EvidencesResource> resourceList = new ArrayList<>();
        resourceList.add(eCertisResource);

        return resourceList;
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

}
