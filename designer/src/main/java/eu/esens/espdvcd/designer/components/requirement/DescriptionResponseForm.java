package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.TextArea;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

public class DescriptionResponseForm extends ResponseForm {
    private DescriptionResponse descriptionResponse = null;
    protected TextArea description = new TextArea("Description: ");

    public DescriptionResponseForm(DescriptionResponse descriptionResponse, String caption, boolean readOnly) {
        this.descriptionResponse = descriptionResponse;
        addComponent(description);
        description.setCaption(caption);
        description.setNullRepresentation("");
        description.setWidth(280, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<DescriptionResponse> binder = new BeanFieldGroup<>(DescriptionResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.descriptionResponse);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);
    }
}
