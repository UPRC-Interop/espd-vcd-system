package eu.esens.espdvcd.designer.DetailsPanel;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.ArrayList;
import java.util.List;

public class DetailsPanelBuilder {
    private String caption = "Details panel";
    private LegislationReference legislationReference = null;
    private List<RequirementGroup> requirementGroups = new ArrayList<>();

    public DetailsPanelBuilder setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public DetailsPanelBuilder setLegislationReference(LegislationReference legislationReference) {
        this.legislationReference = legislationReference;
        return this;
    }

    public DetailsPanelBuilder setRequirementGroups(List<RequirementGroup> requirementGroups) {
        this.requirementGroups = requirementGroups;
        return this;
    }

    public DetailsPanel build() {
        return new DetailsPanel(caption, legislationReference, requirementGroups);
    }
}
