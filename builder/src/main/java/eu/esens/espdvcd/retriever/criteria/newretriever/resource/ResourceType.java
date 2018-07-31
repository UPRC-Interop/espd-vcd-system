package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

public enum ResourceType {

    ECERTIS(1), ARTEFACT(2), TAXONOMY(3);

    private int priority;

    ResourceType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
