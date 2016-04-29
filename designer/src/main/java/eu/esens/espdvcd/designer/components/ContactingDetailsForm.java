package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.ContactingDetails;

/**
 * Created by ixuz on 4/13/16.
 */
public class ContactingDetailsForm extends Panel {

    ContactingDetails contactingDetails = null;

    private VerticalLayout panelContent = new VerticalLayout();

    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();

    private TextField contactPointName = new TextField("Contact point name:");
    private TextField faxNumber = new TextField("Fax number:");
    private TextField telephoneNumber = new TextField("Phone number:");
    private TextField emailAddress = new TextField("Email:");

    public ContactingDetailsForm(ContactingDetails contactingDetails, boolean readOnly) {
        this.contactingDetails = contactingDetails;

        setStyleName("ContactingDetailsForm");
        setCaption("Contracting details form");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(contactPointName);
        columnA.addComponent(emailAddress);
        columnB.addComponent(telephoneNumber);
        columnB.addComponent(faxNumber);

        final BeanFieldGroup<ContactingDetails> binder = new BeanFieldGroup<>(ContactingDetails.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.contactingDetails);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);

        panelContent.setStyleName("ContactingDetailsFormPanelContent");
        panelContent.setSpacing(true);
        panelContent.setMargin(true);
        columns.setWidth("100%");
        columns.setSpacing(true);
        columns.setMargin(false);
        columnA.setSpacing(true);
        columnA.setMargin(false);
        columnB.setSpacing(true);
        columnB.setMargin(false);

        contactPointName.setNullRepresentation("");
        contactPointName.setInputPrompt("Contact point name");
        contactPointName.setWidth(400, Unit.PIXELS);

        emailAddress.setNullRepresentation("");
        emailAddress.setInputPrompt("Email address");
        emailAddress.setWidth(400, Unit.PIXELS);

        telephoneNumber.setNullRepresentation("");
        telephoneNumber.setInputPrompt("Phone number");
        telephoneNumber.setWidth(300, Unit.PIXELS);

        faxNumber.setNullRepresentation("");
        faxNumber.setInputPrompt("Fax number");
        faxNumber.setWidth(300, Unit.PIXELS);
    }
}
