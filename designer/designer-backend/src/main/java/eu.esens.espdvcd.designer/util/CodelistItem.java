package eu.esens.espdvcd.designer.util;

public class CodelistItem {

    private final String code;
    private final String name;

    public CodelistItem(String key, String value) {
        this.code = key;
        this.name = value;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof CodelistItem){
            CodelistItem codelistItem = (CodelistItem) object;
            return this.code.equals(codelistItem.code) && this.name.equals(codelistItem.name);
        }else{
            return false;
        }
    }
}
