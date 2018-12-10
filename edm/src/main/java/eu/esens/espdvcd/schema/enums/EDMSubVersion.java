package eu.esens.espdvcd.schema.enums;

/**
 * {@link EDMSubVersion#V102} refers to ESPD Exchange Data Model (EDM) version 1.0.2
 * {@link EDMSubVersion#V200} refers to ESPD Exchange Data Model (EDM) version 2.0.0
 * {@link EDMSubVersion#V201} refers to ESPD Exchange Data Model (EDM) version 2.0.1
 * {@link EDMSubVersion#V202} refers to ESPD Exchange Data Model (EDM) version 2.0.2
 * {@link EDMSubVersion#V210} refers to ESPD Exchange Data Model (EDM) version 2.1.0
 */
public enum EDMSubVersion {

    V102(EDMVersion.V1, "1.0.2"),

    V210(EDMVersion.V2, "2.1.0"),

    V200(EDMVersion.V2, "2.0.0"),

    V201(EDMVersion.V2, "2.0.1"),

    V202(EDMVersion.V2, "2.0.2");

    private EDMVersion version;
    private String tag;

    EDMSubVersion(EDMVersion version, String tag) {
        this.version = version;
        this.tag = tag;
    }

    public EDMVersion getVersion() {
        return version;
    }

    public String getTag() {
        return tag;
    }

}
