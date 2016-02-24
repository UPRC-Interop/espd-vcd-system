package eu.esens.espdvcd.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroup implements Serializable {
    private String                 ID;
    private ArrayList<Requirement> requirements = new ArrayList<>();

    public RequirementGroup(String ID, ArrayList<Requirement> requirements) {
        this.ID = ID;
        this.requirements = requirements;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Requirement> requirement) {
        this.requirements = requirement;
    }
}