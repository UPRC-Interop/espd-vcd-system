package eu.esens.espdvcd.retriever.criteria.resource.enums;

/**
 * Enum type for cardinality values contained in
 * Exchange Data Model (EDM). Mapping is as follows:
 * <p>
 * 1    : Mandatory = true,  Multiple = false
 * <p>
 * 0..n : Mandatory = false, Multiple = true
 * <p>
 * 0..1 : Mandatory = false, Multiple = false
 * <p>
 * 1..n : Mandatory = true,  Multiple = true
 *
 * @version 2.0.2
 */
public enum CardinalityEnum {

    ONE(true, false),

    ZERO_OR_ONE(false, false),

    ZERO_TO_MANY(false, true),

    ONE_TO_MANY(true, true);

    private boolean mandatory;
    private boolean multiple;

    CardinalityEnum(boolean mandatory, boolean multiple) {
        this.mandatory = mandatory;
        this.multiple = multiple;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isMultiple() {
        return multiple;
    }

}
