package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.QuantityYearResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class QuantityYearResponseForm extends ResponseForm {
    private QuantityYearResponse quantityYearResponse = null;
    private TextField year = new TextField("Quantity Year: ");

    public QuantityYearResponseForm(QuantityYearResponse quantityYearResponse, String caption) {
        this.quantityYearResponse = quantityYearResponse;
        addComponent(year);
        year.setCaption(caption);
        year.setNullRepresentation("");
        year.setWidth(280, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<QuantityYearResponse> binder = new BeanFieldGroup<>(QuantityYearResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.quantityYearResponse);
        binder.setBuffered(false);
    }
}
