package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.QuantityResponse;

public class QuantityResponseForm extends ResponseForm {
    private QuantityResponse quantityResponse = null;
    private TextField quantity = new TextField("Quantity: ");

    public QuantityResponseForm(QuantityResponse quantityResponse, String caption, boolean readOnly) {
        this.quantityResponse = quantityResponse;
        addComponent(quantity);
        quantity.setCaption(caption);
        quantity.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<QuantityResponse> binder = new BeanValidationBinder<>(QuantityResponse.class);

        binder.forField(quantity)
                .withConverter(new StringToFloatConverter("Cannot convert to float"))
                .bind("quantity");
//        binder.bindInstanceFields(this);
        binder.setBean(this.quantityResponse);
    }
}
