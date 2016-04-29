package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import eu.esens.espdvcd.model.requirement.response.DateResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class DateResponseForm extends ResponseForm {
    private DateResponse dateResponse = null;
    private DateField date = new DateField("Date: ");

    public DateResponseForm(DateResponse dateResponse, String caption, boolean readOnly) {
        this.dateResponse = dateResponse;
        addComponent(date);
        date.setCaption(caption);
        date.setWidth(280, Unit.PIXELS);
        date.setDateFormat("dd/MM/yyyy");

        // Bind fields
        final BeanFieldGroup<DateResponse> binder = new BeanFieldGroup<>(DateResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.dateResponse);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);
    }
}

