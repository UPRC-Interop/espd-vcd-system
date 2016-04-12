package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.QuantityResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class QuantityResponseForm extends ResponseForm {
    private QuantityResponse quantityResponse = null;
    private TextField quantity = new TextField("Quantity: ");

    public QuantityResponseForm(QuantityResponse quantityResponse, String caption) {
        this.quantityResponse = quantityResponse;
        addComponent(quantity);
        setCaption(caption);
        quantity.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<QuantityResponse> binder = new BeanFieldGroup<>(QuantityResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.quantityResponse);
        binder.setBuffered(false);
    }
}
