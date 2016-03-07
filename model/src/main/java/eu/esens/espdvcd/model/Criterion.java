package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class Criterion implements Serializable {

    private String ID;
    private String typeCode;
    private String name;
    private String description;
    private LegislationReference legislationReference;
    private List<RequirementGroup> requirementGroups;

    public Criterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups) {
        this.ID = ID;
        this.typeCode = typeCode;
        this.name = name;
        this.description = description;
        this.legislationReference = legislationReference;
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
