package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.TextArea;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

public class DescriptionResponseForm extends ResponseForm {
    private DescriptionResponse descriptionResponse = null;
    protected TextArea description = new TextArea("Description: ");

    public DescriptionResponseForm(DescriptionResponse descriptionResponse, String caption, boolean readOnly) {
        this.descriptionResponse = descriptionResponse;
        addComponent(description);
        description.setCaption(caption);
        description.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<DescriptionResponse> binder = new BeanValidationBinder<>(DescriptionResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.descriptionResponse);
        binder.setReadOnly(readOnly);
    }
}
