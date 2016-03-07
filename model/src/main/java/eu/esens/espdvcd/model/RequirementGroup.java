package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroup implements Serializable {

    private static final long serialVersionUID = -2179415621032376712L;    
    private String ID;
    private List<Requirement> requirements;

    public RequirementGroup(String ID) {
        this.ID = ID;
    }
    
    public RequirementGroup(String ID, List<Requirement> requirements) {
        this.ID = ID;
        this.requirements = requirements;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<Requirement> getRequirements() {
        if (requirements == null) {
            requirements = new ArrayList<>();
        }
        return requirements;
    }

    public void setRequirements(List<Requirement> requirement) {
        this.requirements = requirement;
    }
}
