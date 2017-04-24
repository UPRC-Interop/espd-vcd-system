package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.QuantityIntegerResponse;

public class QuantityIntegerResponseForm extends ResponseForm {
    private QuantityIntegerResponse quantityIntegerResponse = null;
    private TextField quantity = new TextField("Quantity Integer: ");

    public QuantityIntegerResponseForm(QuantityIntegerResponse quantityIntegerResponse, String caption, boolean readOnly) {
        this.quantityIntegerResponse = quantityIntegerResponse;
        addComponent(quantity);
        quantity.setCaption(caption);
        quantity.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<QuantityIntegerResponse> binder = new BeanValidationBinder<>(QuantityIntegerResponse.class);
        binder.forField(quantity)
                .withConverter(new StringToIntegerConverter("Cannot convert to Integer"))
                .bind("quantity");
        binder.setBean(this.quantityIntegerResponse);
        binder.setReadOnly(readOnly);
    }
}
