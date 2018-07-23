package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

public enum ResourceType {

    ECERTIS(3), ARTEFACT(2), TAXONOMY(1);

    private int priority;

    ResourceType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
