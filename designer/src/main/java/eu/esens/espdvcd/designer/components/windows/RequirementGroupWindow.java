package eu.esens.espdvcd.designer.components.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.DetailsPanel.DetailsPanelRequirement;
import eu.esens.espdvcd.designer.DetailsPanel.DetailsPanelRequirementGroup;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequestRequirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

/**
 * Created by ixuz on 6/23/16.
 */
public class RequirementGroupWindow extends Window {

    AbstractLayout destinationLayout = null;
    VerticalLayout windowLayout = new VerticalLayout();

    HorizontalLayout buttonBar = new HorizontalLayout();
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Close");

    public RequirementGroupWindow(AbstractLayout destinationLayout) {
        this.destinationLayout = destinationLayout;

        setCaption("Requirement Group window");
        setContent(windowLayout);
        setWidth(600, Unit.PIXELS);

        center();
        setClosable(false);
        setDraggable(false);
        setResizable(false);
        setModal(true);

        windowLayout.setMargin(true);
        windowLayout.setSpacing(true);
        windowLayout.addComponent(buttonBar);
        buttonBar.addComponent(saveButton);
        buttonBar.addComponent(closeButton);

        saveButton.addClickListener(this::onSave);
        closeButton.addClickListener(this::onClose);
    }

    public void onSave(Button.ClickEvent clickEvent) {
        RequirementGroup requirementGroup = new RequirementGroup("123");
        DetailsPanelRequirementGroup detailsPanelRequirementGroup = new DetailsPanelRequirementGroup(requirementGroup);
        destinationLayout.addComponent(detailsPanelRequirementGroup);

        Notification notification = new Notification("Your requirement group has been added!");
        notification.setPosition(Position.TOP_CENTER);
        notification.setDelayMsec(1000);
        notification.show(Page.getCurrent());

        UI.getCurrent().removeWindow(this);
    }

    public void onClose(Button.ClickEvent clickEvent) {
        UI.getCurrent().removeWindow(this);
    }
}
