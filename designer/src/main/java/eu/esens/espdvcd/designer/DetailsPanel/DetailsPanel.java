package eu.esens.espdvcd.designer.DetailsPanel;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.LegislationReferenceForm;
import eu.esens.espdvcd.designer.components.RequirementGroupForm;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.List;
import java.util.UUID;

public class DetailsPanel extends Panel {

    VerticalLayout detailsPanelLayout = new VerticalLayout();
    Panel requirementGroupsPanel = new Panel();

    VerticalLayout legislationReferenceLayout = new VerticalLayout();
    VerticalLayout requirementGroupsLayout = new VerticalLayout();
    Button newRequirementGroupButton = new Button("Group");

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


        newRequirementGroupButton.setStyleName("detailsPanelRequirementGroupNewButton");
        newRequirementGroupButton.setIcon(FontAwesome.PLUS);
        newRequirementGroupButton.setSizeUndefined();
        requirementGroupsLayout.addComponent(newRequirementGroupButton);

        for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
            requirementGroupsLayout.addComponent(new DetailsPanelRequirementGroup(criterion, null, requirementGroup));
        }


        newRequirementGroupButton.addClickListener((clickEvent) -> {
            RequirementGroup newRequirementGroup = new RequirementGroup(UUID.randomUUID().toString());
            DetailsPanelRequirementGroup detailsPanelRequirementGroup = new DetailsPanelRequirementGroup(criterion, null, newRequirementGroup);
            requirementGroupsLayout.addComponent(detailsPanelRequirementGroup);
            criterion.getRequirementGroups().add(newRequirementGroup);

            Notification notification = new Notification("Your requirement group has been added!");
            notification.setPosition(Position.TOP_CENTER);
            notification.setDelayMsec(1000);
            notification.show(Page.getCurrent());
        });
    }


}
