package eu.esens.espdvcd.designer.model;

public class DocumentDetails {
    private final String version, type;

    public DocumentDetails(String version, String type) {
        this.version = version;
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }
}
