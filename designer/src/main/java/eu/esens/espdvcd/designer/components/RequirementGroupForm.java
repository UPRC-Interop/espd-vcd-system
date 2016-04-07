package eu.esens.espdvcd.designer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.requirement.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroupForm extends VerticalLayout {
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    //private Label ID = new Label("RequirementGroupForm ID");

    public RequirementGroupForm(RequirementGroup requirementGroup) {
        setMargin(true);
        setStyleName("requirementGroupForm-layout");
        addComponent(panel);
        panel.setContent(panelContent);

        //panelContent.addComponent(ID);

        panelContent.setMargin(true);
        panelContent.setStyleName("requirementGroupForm-panelContent");

        this.addLayoutClickListener(this::onRequirementGroupClick);

        //ID.setCaption("Requirement Group ID");
        //ID.setValue(requirementGroup.getID());

        panel.setStyleName("requirementGroupForm-panel");
        panel.setCaption("Requirement group");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);

        // Add a sub requirement forms
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirement);
            panelContent.addComponent(requirementForm);
        }

        // Add a sub requirement group forms
        for (RequirementGroup requirementGroupTemp : requirementGroup.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroupTemp);
            panelContent.addComponent(requirementGroupForm);
        }
    }

    void onRequirementGroupClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof Panel && event.getClickedComponent() == panel) {
            if (!event.isDoubleClick()) {
                panelContent.setVisible(!panelContent.isVisible());

                if (panelContent.isVisible()) {
                    panel.setIcon(FontAwesome.CHEVRON_DOWN);
                } else {
                    panel.setIcon(FontAwesome.CHEVRON_RIGHT);
                }
            }
        }
    }
}
