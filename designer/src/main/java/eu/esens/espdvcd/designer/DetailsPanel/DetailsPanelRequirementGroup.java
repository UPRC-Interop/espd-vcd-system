package eu.esens.espdvcd.designer.DetailsPanel;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.components.windows.RequirementGroupWindow;
import eu.esens.espdvcd.designer.components.windows.RequirementWindow;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

import java.util.UUID;

public class DetailsPanelRequirementGroup extends VerticalLayout {

    AbstractLayout parentLayout = null;
    HorizontalLayout buttonBar = new HorizontalLayout();
    HorizontalLayout buttonBarLeft = new HorizontalLayout();
    HorizontalLayout buttonBarRight = new HorizontalLayout();
    VerticalLayout requirementsLayout = new VerticalLayout();
    VerticalLayout requirementGroupsLayout = new VerticalLayout();
    Button newRequirementGroupButton = new Button("Group");
    Button newRequirementButton = new Button("Requirement");
    Button deleteRequirementGroupButton = new Button("");

    public DetailsPanelRequirementGroup(RequirementGroup requirementGroup) {
        setStyleName("detailsPanelRequirementGroup");

        buttonBar.setSizeFull();

        newRequirementGroupButton.setStyleName("detailsPanelRequirementGroupNewButton");
        newRequirementGroupButton.setIcon(FontAwesome.PLUS);
        newRequirementGroupButton.setSizeUndefined();

        newRequirementButton.setStyleName("detailsPanelRequirementNewButton");
        newRequirementButton.setIcon(FontAwesome.PLUS);
        newRequirementButton.setSizeUndefined();

        deleteRequirementGroupButton.setStyleName("detailsPanelRequirementGroupDeleteButton");
        deleteRequirementGroupButton.setIcon(FontAwesome.REMOVE);

        addComponent(buttonBar);
        buttonBar.addComponent(buttonBarLeft);
        buttonBar.addComponent(buttonBarRight);
        buttonBar.setComponentAlignment(buttonBarLeft, Alignment.TOP_LEFT);
        buttonBar.setComponentAlignment(buttonBarRight, Alignment.TOP_RIGHT);

        buttonBarLeft.addComponent(newRequirementGroupButton);
        buttonBarLeft.addComponent(newRequirementButton);
        buttonBarLeft.setComponentAlignment(newRequirementGroupButton, Alignment.TOP_RIGHT);
        buttonBarLeft.setComponentAlignment(newRequirementButton, Alignment.TOP_RIGHT);

        buttonBarRight.addComponent(deleteRequirementGroupButton);
        buttonBarRight.setComponentAlignment(deleteRequirementGroupButton, Alignment.TOP_RIGHT);

        addComponent(requirementsLayout);
        addComponent(requirementGroupsLayout);

        requirementsLayout.setMargin(true);
        requirementsLayout.setSpacing(true);
        requirementGroupsLayout.setMargin(true);
        requirementGroupsLayout.setSpacing(true);

        for (Requirement requirement : requirementGroup.getRequirements()) {
            requirementsLayout.addComponent(new DetailsPanelRequirement(requirement));
        }

        for (RequirementGroup subRequirementGroup : requirementGroup.getRequirementGroups()) {
            requirementGroupsLayout.addComponent(new DetailsPanelRequirementGroup(subRequirementGroup));
        }

        newRequirementGroupButton.addClickListener((clickEvent) -> {
            RequirementGroup newRequirementGroup = new RequirementGroup(UUID.randomUUID().toString());
            DetailsPanelRequirementGroup detailsPanelRequirementGroup = new DetailsPanelRequirementGroup(newRequirementGroup);
            requirementGroupsLayout.addComponent(detailsPanelRequirementGroup);

            Notification notification = new Notification("Your requirement group has been added!");
            notification.setPosition(Position.TOP_CENTER);
            notification.setDelayMsec(1000);
            notification.show(Page.getCurrent());
        });

        newRequirementButton.addClickListener((clickEvent) -> {
            RequirementWindow requirementWindow = new RequirementWindow(requirementsLayout);
            UI.getCurrent().addWindow(requirementWindow);
        });

        deleteRequirementGroupButton.addClickListener((clickEvent) -> {
            ComponentContainer parent = (ComponentContainer) getParent();
            parent.removeComponent(this);
        });
    }
}
