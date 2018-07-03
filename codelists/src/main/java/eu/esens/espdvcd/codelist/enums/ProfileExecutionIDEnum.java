package eu.esens.espdvcd.codelist.enums;

/**
 * The ProfileExecutionIDEnum provides a compile time enumeration of the available
 * profile execution ids (profile execution id refers to artefact version).
 *
 * @version 2.0.2
 * @since 2.0.2
 */
public enum ProfileExecutionIDEnum {

    // 2.0.0
    ESPD_EDM_V2_0_0_REGULATED("ESPD-EDMv2.0.0-REGULATED"),
    ESPD_EDM_V2_0_0_SELFCONTAINED("ESPD-EDMv2.0.0-SELFCONTAINED"),
    // 2.0.1
    ESPD_EDM_V2_0_1_REGULATED("ESPD-EDMv2.0.1-REGULATED"),
    ESPD_EDM_V2_0_1_SELFCONTAINED("ESPD-EDMv2.0.1-SELFCONTAINED"),
    // 2.0.2
    ESPD_EDM_V2_0_2_REGULATED("ESPD-EDMv2.0.2-REGULATED"),
    ESPD_EDM_V2_0_2_SELFCONTAINED("ESPD-EDMv2.0.2-SELFCONTAINED"),
    // 1.0.2
    ESPD_EDM_V1_0_2("ESPD-EDMv1.0.2");

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
