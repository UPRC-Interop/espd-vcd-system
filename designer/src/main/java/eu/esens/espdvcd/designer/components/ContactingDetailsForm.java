package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.ContactingDetails;

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

    public ContactingDetailsForm(ContactingDetails contactingDetails, boolean readOnly, boolean useContactPointName) {
        this.contactingDetails = contactingDetails;

        setStyleName("ContactingDetailsForm");
        setCaption("Contracting details form");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        if (useContactPointName) {
            columnA.addComponent(contactPointName);
        }
        columnA.addComponent(emailAddress);
        columnB.addComponent(telephoneNumber);
        columnB.addComponent(faxNumber);

        final Binder<ContactingDetails> binder = new BeanValidationBinder<>(ContactingDetails.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.contactingDetails);
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

        /*contactPointName.setWidth(400, Unit.PIXELS);

        emailAddress.setWidth(400, Unit.PIXELS);

        telephoneNumber.setWidth(300, Unit.PIXELS);

        faxNumber.setWidth(300, Unit.PIXELS);*/

        contactPointName.setWidth("17em");
        //contactPointName.setStyleName("textFieldCaptionText");
        emailAddress.setWidth("17em");
        //emailAddress.setStyleName("textFieldCaptionText");
        telephoneNumber.setWidth("17em");
        //telephoneNumber.setStyleName("textFieldCaptionText");
        faxNumber.setWidth("17em");
        //faxNumber.setStyleName("textFieldCaptionText");
    }
}
