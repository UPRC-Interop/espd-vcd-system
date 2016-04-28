package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.designer.components.CurrencyComboBox;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class AmountResponseForm extends ResponseForm {
    private AmountResponse amountResponse = null;
    private TextField amount = new TextField("Amount: ");
    private CurrencyComboBox currency = new CurrencyComboBox("Currency: ");

    public AmountResponseForm(AmountResponse amountResponse, String caption) {
        this.amountResponse = amountResponse;
        addComponent(amount);
        addComponent(currency);
        amount.setCaption(caption);
        amount.setNullRepresentation("");
        amount.setWidth(280, Unit.PIXELS);
        currency.setWidth(280, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<AmountResponse> binder = new BeanFieldGroup<>(AmountResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.amountResponse);
        binder.setBuffered(false);
    }
}