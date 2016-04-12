package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class AmountResponseForm extends ResponseForm {
    private AmountResponse amountResponse = null;
    private TextField amount = new TextField("Amount: ");
    private TextField currency = new TextField("Currency: ");

    public AmountResponseForm(AmountResponse amountResponse, String caption) {
        this.amountResponse = amountResponse;
        addComponent(amount);
        addComponent(currency);
        setCaption(caption);
        amount.setNullRepresentation("");
        currency.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<AmountResponse> binder = new BeanFieldGroup<>(AmountResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.amountResponse);
        binder.setBuffered(false);
    }
}
