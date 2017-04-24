package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.RadioButtonGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IndicatorResponseForm extends ResponseForm {
    private IndicatorResponse indicatorResponse = null;
    protected RadioButtonGroup<Boolean> indicator = new RadioButtonGroup<>("Indicator: ");

    public IndicatorResponseForm(IndicatorResponse indicatorResponse, String caption, boolean readOnly) {
        this.indicatorResponse = indicatorResponse;

        indicator.setCaption(caption);
        indicator.setStyleName("horizontal");
        indicator.setItems(Arrays.asList(true,false));
        indicator.setItemCaptionGenerator(item -> (item == true) ? "Yes" : "No" );
        
        addComponent(indicator);

        // Bind fields
        final Binder<IndicatorResponse> binder = new BeanValidationBinder<>(IndicatorResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.indicatorResponse);
        binder.setReadOnly(readOnly);
    }
}
