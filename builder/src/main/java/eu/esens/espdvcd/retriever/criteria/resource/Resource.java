package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceType;

/**
 * @author Konstantinos Raptis
 */
public interface Resource {

    /**
     * Resource type refers to the origin from where the resource will be retrieved.
     *
     * @return The resource type
     */
    ResourceType getResourceType();

}
