package eu.esens.espdvcd.designer.util;

public class PlatformInfo {
    private String version;
    private String revision;
    private String name;
    private String buildTime;

    public PlatformInfo(String version, String revision, String name, String buildTime) {
        this.version = version;
        this.revision = revision;
        this.name = name;
        this.buildTime = buildTime;
    }

    public String getVersion() {
        return version;
    }

    public String getRevision() {
        return revision;
    }

    public String getName() {
        return name;
    }

    public String getBuildTime() {
        return buildTime;
    }
}
