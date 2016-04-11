package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.QuantityYearResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class QuantityYearResponseForm extends FormLayout {
    private QuantityYearResponse quantityYearResponse = null;
    private TextField year = new TextField("Quantity Year: ");

    public QuantityYearResponseForm(QuantityYearResponse quantityYearResponse) {
        this.quantityYearResponse = quantityYearResponse;
        addComponent(year);

        // Bind fields
        final BeanFieldGroup<QuantityYearResponse> binder = new BeanFieldGroup<>(QuantityYearResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.quantityYearResponse);
        binder.setBuffered(false);
    }
}
