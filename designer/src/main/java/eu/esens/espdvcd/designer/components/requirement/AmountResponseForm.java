package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.CurrencyComboBox;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class AmountResponseForm extends ResponseForm {
    private AmountResponse amountResponse = null;
    private TextField amount = new TextField("Amount: ");
    private CurrencyComboBox currency = new CurrencyComboBox("Currency: ");
    private HorizontalLayout columns = new HorizontalLayout();
    private VerticalLayout columnA = new VerticalLayout();
    private VerticalLayout columnB = new VerticalLayout();

    public AmountResponseForm(AmountResponse amountResponse, String caption) {
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

        amount.setNullRepresentation("");
        amount.setWidth(120, Unit.PIXELS);
        currency.setWidth(160, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<AmountResponse> binder = new BeanFieldGroup<>(AmountResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.amountResponse);
        binder.setBuffered(false);
    }
}
