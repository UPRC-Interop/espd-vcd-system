package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.PercentageResponse;

public class PercentageResponseForm extends ResponseForm {
    private PercentageResponse percentageResponse = null;
    private TextField percentage = new TextField("Percentage: ");

    public PercentageResponseForm(PercentageResponse percentageResponse, String caption, boolean readOnly) {
        this.percentageResponse = percentageResponse;
        addComponent(percentage);
        percentage.setCaption(caption);
        percentage.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<PercentageResponse> binder = new BeanValidationBinder<>(PercentageResponse.class);
        binder.forField(percentage)
                .withConverter(new StringToFloatConverter("Cannot convert to number"))
                .bind("percentage");
        binder.setBean(this.percentageResponse);
        binder.setReadOnly(readOnly);
    }
}
