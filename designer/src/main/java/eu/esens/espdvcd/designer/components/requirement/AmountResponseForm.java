package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.CurrencyComboBox;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;

public class AmountResponseForm extends ResponseForm {
    private AmountResponse amountResponse = null;
    private TextField amount = new TextField("Amount: ");
    private CurrencyComboBox currency = new CurrencyComboBox("Currency: ");
    private HorizontalLayout columns = new HorizontalLayout();
    private VerticalLayout columnA = new VerticalLayout();
    private VerticalLayout columnB = new VerticalLayout();

    public AmountResponseForm(AmountResponse amountResponse, String caption, boolean readOnly) {
        this.amountResponse = amountResponse;
        addComponent(columns);
        columns.setCaption(caption);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(amount);
        columnB.addComponent(currency);

        columns.setMargin(false);
        columnA.setMargin(false);
        columnB.setMargin(false);

        amount.setWidth(120, Unit.PIXELS);
        currency.setWidth(160, Unit.PIXELS);

        Binder<AmountResponse> binder = new BeanValidationBinder<>(AmountResponse.class);
        binder.forMemberField(amount)
                .withConverter(new StringToFloatConverter("Cannot convert string to float"));
        binder.bindInstanceFields(this);
        binder.setBean(this.amountResponse);
        binder.setReadOnly(readOnly);
    }
}
