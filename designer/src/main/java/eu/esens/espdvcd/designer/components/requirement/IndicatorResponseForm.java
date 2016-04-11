package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import eu.esens.espdvcd.model.requirement.IndicatorResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class IndicatorResponseForm extends FormLayout {
    private IndicatorResponse indicatorResponse = null;
    protected CheckBox indicator = new CheckBox("Indicator: ");

    public IndicatorResponseForm(IndicatorResponse indicatorResponse) {
        this.indicatorResponse = indicatorResponse;
        addComponent(indicator);

        // Bind fields
        final BeanFieldGroup<IndicatorResponse> binder = new BeanFieldGroup<>(IndicatorResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.indicatorResponse);
        binder.setBuffered(false);
    }
}
