package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.components.requirement.ReadOnlyResponseForm;
import eu.esens.espdvcd.designer.components.requirement.ResponseForm;
import eu.esens.espdvcd.designer.components.requirement.ResponseFormFactory;
import eu.esens.espdvcd.model.requirement.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

public class RequirementForm extends VerticalLayout {

    public RequirementForm(Requirement requirement, boolean includeResponses, boolean readOnly) {
        setStyleName("requirementForm-layout");
        if (includeResponses) {
            ResponseForm responseForm = ResponseFormFactory.buildResponseForm(requirement, readOnly);
            addComponent(responseForm);
        } else {
            ResponseForm responseForm = new ReadOnlyResponseForm(requirement.getResponseDataType().name(), requirement.getDescription());
            addComponent(responseForm);
        }

        // Bind the this forms fields
        final BeanFieldGroup<Requirement> requirementGroup = new BeanFieldGroup<>(Requirement.class);
        requirementGroup.setItemDataSource(requirement);
        requirementGroup.setBuffered(false);
        requirementGroup.bindMemberFields(this);
        requirementGroup.setReadOnly(readOnly);
    }
}
