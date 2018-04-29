package eu.esens.espdvcd.schema;

public enum SchemaVersion {

    V1("1.0.2"), V2("2.0.1");

    private String tag;

    SchemaVersion(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag;
    }

}
