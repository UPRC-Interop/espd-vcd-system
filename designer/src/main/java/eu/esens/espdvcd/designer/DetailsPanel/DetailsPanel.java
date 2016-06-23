package eu.esens.espdvcd.designer.DetailsPanel;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.LegislationReferenceForm;
import eu.esens.espdvcd.designer.components.RequirementGroupForm;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.List;

public class DetailsPanel extends Panel {

    VerticalLayout detailsPanelLayout = new VerticalLayout();
    Panel requirementGroupsPanel = new Panel();

    VerticalLayout legislationReferenceLayout = new VerticalLayout();
    VerticalLayout requirementGroupsLayout = new VerticalLayout();

    public DetailsPanel(String caption, LegislationReference legislationReference, List<RequirementGroup> requirementGroupList) {
        setContent(detailsPanelLayout);
        setCaption(caption);

        detailsPanelLayout.addComponent(legislationReferenceLayout);
        detailsPanelLayout.addComponent(requirementGroupsPanel);

        requirementGroupsPanel.setCaption("Criterion requirements");
        requirementGroupsPanel.setContent(requirementGroupsLayout);
        requirementGroupsLayout.setMargin(true);
        requirementGroupsLayout.setSpacing(true);

        if (legislationReference != null) {
            LegislationReferenceForm legislationReferenceForm = new LegislationReferenceForm(legislationReference);
            legislationReferenceLayout.addComponent(legislationReferenceForm);
        }

        for (RequirementGroup requirementGroup : requirementGroupList) {
            //requirementGroupsLayout.addComponent(new RequirementGroupForm(requirementGroup, false, true));
            requirementGroupsLayout.addComponent(new DetailsPanelRequirementGroup(requirementGroup));
        }
    }


}
