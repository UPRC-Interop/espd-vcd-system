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
        setMargin(true);
        setStyleName("requirementGroupForm-layout");

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);

        // Add a sub requirement forms
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirement, includeResponses);
            addComponent(requirementForm);
        }

        // Add a sub requirement group forms
        for (RequirementGroup requirementGroupTemp : requirementGroup.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroupTemp, includeResponses);
            addComponent(requirementGroupForm);
        }
    }
}
