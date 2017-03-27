package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.PostalAddress;

public class PostalAddressForm extends Panel {

    private PostalAddress postalAddress = null;
    private VerticalLayout panelContent = new VerticalLayout();
    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();
    private TextField addressLine1 = new TextField("Address line:");
    private TextField city = new TextField("City:");
    private TextField postCode = new TextField("Post code:");
    private CountryComboBox countryCode = new CountryComboBox("Country:");

    public PostalAddressForm(PostalAddress postalAddress, boolean readOnly) {
        this.postalAddress = postalAddress;

        setStyleName("PostalAddressForm");
        setCaption("Postal address form");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(addressLine1);
        columnA.addComponent(city);
        columnB.addComponent(countryCode);
        columnB.addComponent(postCode);

        final Binder<PostalAddress> binder = new BeanValidationBinder<>(PostalAddress.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.postalAddress);
        binder.setReadOnly(readOnly);

        setWidth("100%");
        panelContent.setStyleName("PostalAddressFormPanelContent");
        panelContent.setSpacing(true);
        panelContent.setMargin(true);
        columns.setWidth("100%");
        columns.setSpacing(true);
        columns.setMargin(false);
        columnA.setSpacing(true);
        columnA.setMargin(false);
        columnB.setSpacing(true);
        columnB.setMargin(false);

        addressLine1.setPlaceholder("Address line");
        addressLine1.setWidth(400, Unit.PIXELS);

        city.setPlaceholder("City");
        city.setWidth(400, Unit.PIXELS);

        postCode.setPlaceholder("Post code");
        postCode.setWidth(400, Unit.PIXELS);

        countryCode.setPlaceholder("Select country");
        countryCode.setWidth(300, Unit.PIXELS);
    }
}
