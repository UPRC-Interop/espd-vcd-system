package eu.esens.espdvcd.model;

/**
 * Created by ixuz on 2/24/16.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Criteria implements Serializable {
    String                      ID;
    String                      typeCode;
    String                      name;
    String                      description;
    List<RequirementGroup> requirementGroups;

    public Criteria(String ID, String typeCode, String name, String description, List<RequirementGroup> requirementGroups) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.description = description;
        this.requirementGroups = requirementGroups;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RequirementGroup> getRequirementGroups() {
        return requirementGroups;
    }

    public void setRequirementGroups(List<RequirementGroup> requirementGroups) {
        this.requirementGroups = requirementGroups;
    }
}
