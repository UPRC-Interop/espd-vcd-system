package eu.esens.espdvcd.designer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroupForm extends VerticalLayout {

    public RequirementGroupForm(RequirementGroup requirementGroup, boolean includeResponses) {

        setMargin(false);
        setSpacing(true);
        setStyleName("requirementGroupForm-layout");
        addStyleName("ignoreCaptionCellWidth");

        setWidth("100%");

        //panelContent.addComponent(ID);

        //ID.setCaption("Requirement Group ID");
        //ID.setValue(requirementGroup.getID());
        //panelContent.setSizeUndefined();

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);


        Panel panel = new Panel();
        panel.setStyleName("RequirementGroupFormPanel");
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        panel.setContent(panelContent);
        addComponent(panel);

        // Add a sub requirement forms
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirement, includeResponses);
            panelContent.addComponent(requirementForm);
        }

        // Add a sub requirement group forms
        for (RequirementGroup requirementGroupTemp : requirementGroup.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroupTemp, includeResponses);
            panelContent.addComponent(requirementGroupForm);
        }
    }
}
