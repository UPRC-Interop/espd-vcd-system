package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.converter.StringToIntegerConverter;
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
        year.setNullRepresentation("");
        year.setWidth(280, Unit.PIXELS);

        StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            };
        };
        year.setConverter(plainIntegerConverter);

        // Bind fields
        final BeanFieldGroup<QuantityYearResponse> binder = new BeanFieldGroup<>(QuantityYearResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.quantityYearResponse);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);
    }
}
