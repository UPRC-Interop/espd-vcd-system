package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.DescriptionResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class DescriptionResponseForm extends FormLayout {
    private DescriptionResponse descriptionResponse = null;
    protected TextField description = new TextField("Description: ");

    public DescriptionResponseForm(DescriptionResponse descriptionResponse, String caption) {
        this.descriptionResponse = descriptionResponse;
        addComponent(description);
        setCaption(caption);
        description.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<DescriptionResponse> binder = new BeanFieldGroup<>(DescriptionResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.descriptionResponse);
        binder.setBuffered(false);
    }
}
