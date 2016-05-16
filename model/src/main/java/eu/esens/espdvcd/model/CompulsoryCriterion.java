package eu.esens.espdvcd.model;

/**
 * Created by Ulf Lotzmann on 05/03/2016.
 */

public class CompulsoryCriterion extends SelectableCriterion {

    private static final long serialVersionUID = -3642428067998066099L;

        public CompulsoryCriterion(String ID, String typeCode, String name, String description, LegislationReference legislationReference) {
        super(ID, typeCode, name, description, legislationReference);
        selected = true;
    }
        
    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = true;
    }
}
