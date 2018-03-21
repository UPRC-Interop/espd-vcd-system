package eu.esens.espdvcd.designer.models;

public class CodelistItem {
    private String code, name;

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
