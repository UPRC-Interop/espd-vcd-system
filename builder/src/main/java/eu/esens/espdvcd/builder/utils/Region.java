package eu.esens.espdvcd.builder.utils;

/**
 *
 * @author Konstantinos Raptis
 */
public class Region {
    
    private String code;
    private String id;
    private String name;

    public Region() {
    }

    public Region(String code, String id, String name) {
        this.code = code;
        this.id = id;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Region{" + "code=" + code + ", id=" + id + ", name=" + name + '}';
    }
       
}
