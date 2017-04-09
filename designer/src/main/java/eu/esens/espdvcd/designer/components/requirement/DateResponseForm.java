package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.DateField;
import eu.esens.espdvcd.model.requirement.response.DateResponse;

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
        final Binder<DateResponse> binder = new BeanValidationBinder<>(DateResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.dateResponse);
        binder.setReadOnly(readOnly);
    }
}

