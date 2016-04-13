package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.model.ContactingDetails;

/**
 * Created by ixuz on 4/13/16.
 */
public class ContactingDetailsForm extends VerticalLayout {

    ContactingDetails contactingDetails = null;
    private TextField contactPointName = new TextField("Contact point name:");
    private TextField faxNumber = new TextField("Fax number:");
    private TextField telephoneNumber = new TextField("Phone number:");
    private TextField emailAddress = new TextField("Email:");

    public ContactingDetailsForm(ContactingDetails contactingDetails) {
        this.contactingDetails = contactingDetails;

        setCaption("Contracting details form");

        addComponent(contactPointName);
        addComponent(faxNumber);
        addComponent(telephoneNumber);
        addComponent(emailAddress);

        final BeanFieldGroup<ContactingDetails> binder = new BeanFieldGroup<>(ContactingDetails.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.contactingDetails);
        binder.setBuffered(false);
    }
}
