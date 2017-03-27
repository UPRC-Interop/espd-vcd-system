package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.QuantityYearResponse;

import java.text.NumberFormat;
import java.util.Locale;

public class QuantityYearResponseForm extends ResponseForm {
    private QuantityYearResponse quantityYearResponse = null;
    private TextField year = new TextField("Quantity Year: ");

    public QuantityYearResponseForm(QuantityYearResponse quantityYearResponse, String caption, boolean readOnly) {
        this.quantityYearResponse = quantityYearResponse;
        addComponent(year);
        year.setCaption(caption);
        year.setWidth(280, Unit.PIXELS);

        StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter("Not an Integer Number") {
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            };
        };

        // Bind fields
        final Binder<QuantityYearResponse> binder = new BeanValidationBinder<>(QuantityYearResponse.class);
        binder.forField(year).withConverter(plainIntegerConverter).bind("year");
       
        binder.setBean(quantityYearResponse);
        binder.setReadOnly(readOnly);
    }
}
