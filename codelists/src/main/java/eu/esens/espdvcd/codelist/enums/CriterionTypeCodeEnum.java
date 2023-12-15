package eu.esens.espdvcd.codelist.enums;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 12/11/2020.
 */
public enum CriterionTypeCodeEnum {

    CRITERION_SELECTION("CRITERION.SELECTION"),
    CRITERION_EXCLUSION("CRITERION.EXCLUSION"),
    CRITERION_OTHER_EO("CRITERION.OTHER.EO"),
    CRITERION_SELECTION_ALPHA("CRITERION.SELECTION.ALL");

    private String value;

    CriterionTypeCodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
