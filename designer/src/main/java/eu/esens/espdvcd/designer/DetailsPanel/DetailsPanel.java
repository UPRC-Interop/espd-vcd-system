package eu.esens.espdvcd.designer.DetailsPanel;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.LegislationReferenceForm;
import eu.esens.espdvcd.designer.components.RequirementGroupForm;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.List;

public class DetailsPanel extends Panel {

    VerticalLayout detailsPanelLayout = new VerticalLayout();
    Panel requirementGroupsPanel = new Panel();

    VerticalLayout legislationReferenceLayout = new VerticalLayout();
    VerticalLayout requirementGroupsLayout = new VerticalLayout();

    public DetailsPanel(Criterion criterion) {
        setContent(detailsPanelLayout);
        setCaption(criterion.getName());

        detailsPanelLayout.addComponent(legislationReferenceLayout);
        detailsPanelLayout.addComponent(requirementGroupsPanel);

        requirementGroupsPanel.setCaption("Criterion requirements");
        requirementGroupsPanel.setContent(requirementGroupsLayout);
        requirementGroupsLayout.setMargin(true);
        requirementGroupsLayout.setSpacing(true);

        if (criterion.getLegislationReference() != null) {
            LegislationReferenceForm legislationReferenceForm = new LegislationReferenceForm(criterion.getLegislationReference());
            legislationReferenceLayout.addComponent(legislationReferenceForm);
        }

        for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
            requirementGroupsLayout.addComponent(new DetailsPanelRequirementGroup(criterion, null, requirementGroup));
        }
    }


}
