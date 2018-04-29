package eu.esens.espdvcd.designer.model;

public final class CodelistItem {
    private final String code, name;

    public CodelistItem(String key, String value){
        this.code = key;
        this.name = value;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
