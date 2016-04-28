package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.OptionGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class IndicatorResponseForm extends ResponseForm {
    private IndicatorResponse indicatorResponse = null;
    protected OptionGroup indicator = new OptionGroup("Indicator: ");

    public IndicatorResponseForm(IndicatorResponse indicatorResponse, String caption) {
        this.indicatorResponse = indicatorResponse;
/*        addComponent(indicator);
        indicator.setCaption(caption);
*/

        indicator.setCaption(caption);
        indicator.setStyleName("horizontal");
        boolean option1 = true;
        boolean option2 = false;
        indicator.addItem(option1);
        indicator.addItem(option2);
        indicator.setItemCaption(option1, "Yes");
        indicator.setItemCaption(option2, "No");
        addComponent(indicator);

        // Bind fields
        final BeanFieldGroup<IndicatorResponse> binder = new BeanFieldGroup<>(IndicatorResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.indicatorResponse);
        binder.setBuffered(false);
    }
}