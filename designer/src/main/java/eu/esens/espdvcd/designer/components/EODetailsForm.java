package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;
import java.util.List;

public class EODetailsForm extends Panel {

    EODetails eoDetails = null;

    private VerticalLayout panelContent = new VerticalLayout();

    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();

    private TextField ID = new TextField("ID:");
    private TextField electronicAddressID = new TextField("Electronic address id:");
    private TextField name = new TextField("Name:");
    private TextField role = new TextField("Role:");
    private CheckBox smeIndicator = new CheckBox("SME:");

    private ContactingDetails contactingDetails; // To be implemented
    private List<NaturalPerson> naturalPersons; // To be implemented

    public EODetailsForm(EODetails eoDetails, boolean readOnly) {
        this.eoDetails = eoDetails;

        setStyleName("EODetailsForm");
        setCaption("Economic operator details");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(name);
        columnA.addComponent(ID);
        columnA.addComponent(electronicAddressID);
        columnB.addComponent(role);
        columnB.addComponent(smeIndicator);

        PostalAddress eoDetailsPostalAddress = eoDetails.getPostalAddress();
        if (eoDetailsPostalAddress != null) {
            panelContent.addComponent(new PostalAddressForm(eoDetailsPostalAddress, readOnly));
        }

        ContactingDetails eoDetailsContactingDetails = eoDetails.getContactingDetails();
        if (eoDetailsContactingDetails != null) {
            panelContent.addComponent(new ContactingDetailsForm(eoDetailsContactingDetails, readOnly, true));
        }

        for (NaturalPerson naturalPerson : this.eoDetails.getNaturalPersons()) {
            panelContent.addComponent(new NaturalPersonForm(naturalPerson, readOnly));
        }

        final BeanFieldGroup<EODetails> binder = new BeanFieldGroup<>(EODetails.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.eoDetails);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);

        panelContent.setStyleName("EODetailsFormPanelContent");
        panelContent.setSpacing(true);
        panelContent.setMargin(true);
        columns.setWidth("100%");
        columns.setSpacing(true);
        columns.setMargin(false);
        columnA.setSpacing(true);
        columnA.setMargin(false);
        columnB.setSpacing(true);
        columnB.setMargin(false);

        ID.setNullRepresentation("");
        ID.setInputPrompt("Please enter ID");
        ID.setWidth(300, Unit.PIXELS);

        electronicAddressID.setNullRepresentation("");
        electronicAddressID.setInputPrompt("Electronic address ID");
        electronicAddressID.setWidth(300, Unit.PIXELS);

        name.setNullRepresentation("");
        name.setInputPrompt("Name");
        name.setWidth(300, Unit.PIXELS);

        role.setNullRepresentation("");
        role.setInputPrompt("Role");
        role.setWidth(400, Unit.PIXELS);
    }
}
