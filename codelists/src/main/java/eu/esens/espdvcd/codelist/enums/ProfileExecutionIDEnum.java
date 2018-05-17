package eu.esens.espdvcd.codelist.enums;

/**
 * The ProfileExecutionIDEnum provides a compile time enumeration of the available
 * profile execution ids (profile execution id refers to artefact version).
 *
 * @version 2.0.2
 * @since 2.0.2
 */
public enum ProfileExecutionIDEnum {

    ESPD_EDM_V2_0_0_REGULATED("ESPD-EDMv2.0.0-REGULATED"),
    ESPD_EDM_V2_0_0_SELFCONTAINED("ESPD-EDMv2.0.0-SELFCONTAINED"),
    ESPD_EDM_V2_0_2_REGULATED("ESPD-EDMv2.0.2-REGULATED"),
    ESPD_EDM_V2_0_2_SELFCONTAINED("ESPD-EDMv2.0.2-SELFCONTAINED"),
    ESPD_EDM_V1_0_2("ESPD-EDMv1.0.2"),
    // unknown enum value is not part of the relevant codelist
    UNKNOWN("UNKNOWN");

    private String value;

    ProfileExecutionIDEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
