package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.RequirementGroup;
import java.util.List;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */
public class SelectableCriterion extends Criterion {

    private static final long serialVersionUID = -4642428067998066099L;

    private boolean selected;

    public SelectableCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference) {
        super(ID, typeCode, name, description, legislationReference);
        selected = false;
    }
    
    public SelectableCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups) {
        super(ID, typeCode, name, description, legislationReference, requirementGroups);
        selected = false;
    }

    public SelectableCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference, List<RequirementGroup> requirementGroups, boolean selected) {
        super(ID, typeCode, name, description, legislationReference, requirementGroups);
        this.selected = selected;
    }

    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
