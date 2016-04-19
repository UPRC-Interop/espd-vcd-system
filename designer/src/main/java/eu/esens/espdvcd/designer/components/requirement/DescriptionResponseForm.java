package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class DescriptionResponseForm extends ResponseForm {
    private DescriptionResponse descriptionResponse = null;
    protected TextArea description = new TextArea("Description: ");

    public DescriptionResponseForm(DescriptionResponse descriptionResponse, String caption) {
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
    }
}
