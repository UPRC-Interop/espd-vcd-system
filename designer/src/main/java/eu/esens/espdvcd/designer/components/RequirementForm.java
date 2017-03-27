package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.components.requirement.ReadOnlyResponseForm;
import eu.esens.espdvcd.designer.components.requirement.ResponseForm;
import eu.esens.espdvcd.designer.components.requirement.ResponseFormFactory;
import eu.esens.espdvcd.model.requirement.Requirement;

public class RequirementForm extends VerticalLayout {

    public RequirementForm(Requirement requirement, boolean includeResponses, int displayEvidences, boolean readOnly) {
        setStyleName("requirementForm-layout");
        if (includeResponses) {
            ResponseForm responseForm = ResponseFormFactory.buildResponseForm(requirement, displayEvidences, readOnly);
            addComponent(responseForm);
        } else {
            ResponseForm responseForm = new ReadOnlyResponseForm(requirement.getResponseDataType().name(), requirement.getDescription());
            addComponent(responseForm);
        }
    }
}
