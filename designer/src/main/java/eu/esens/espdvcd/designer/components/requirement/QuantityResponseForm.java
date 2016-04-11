package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.QuantityResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class QuantityResponseForm extends FormLayout {
    private QuantityResponse quantityResponse = null;
    private TextField quantity = new TextField("Quantity: ");

    public QuantityResponseForm(QuantityResponse quantityResponse) {
        this.quantityResponse = quantityResponse;
        addComponent(quantity);

        // Bind fields
        final BeanFieldGroup<QuantityResponse> binder = new BeanFieldGroup<>(QuantityResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.quantityResponse);
        binder.setBuffered(false);
    }
}
