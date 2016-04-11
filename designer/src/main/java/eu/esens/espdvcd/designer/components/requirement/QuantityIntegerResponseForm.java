package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.QuantityIntegerResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class QuantityIntegerResponseForm extends FormLayout {
    private QuantityIntegerResponse quantityIntegerResponse = null;
    private TextField quantity = new TextField("Quantity Integer: ");

    public QuantityIntegerResponseForm(QuantityIntegerResponse quantityIntegerResponse, String caption) {
        this.quantityIntegerResponse = quantityIntegerResponse;
        addComponent(quantity);
        setCaption(caption);
        quantity.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<QuantityIntegerResponse> binder = new BeanFieldGroup<>(QuantityIntegerResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.quantityIntegerResponse);
        binder.setBuffered(false);
    }
}
