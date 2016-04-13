package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;

import java.util.List;

/**
 * Created by ixuz on 4/13/16.
 */
public class EODetailsForm extends Panel {

    EODetails eoDetails = null;

    private VerticalLayout panelContent = new VerticalLayout();

    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();

    private TextField ID = new TextField("ID:");
    private TextField electronicAddressID = new TextField("Electronic address id:");
    private CountryComboBox registrationCountryCode = new CountryComboBox("Country:");
    private TextField name = new TextField("Name:");
    private TextField role = new TextField("Role:");
    private CheckBox smeIndicator = new CheckBox("SME:");
    private TextField nationalDatabaseURI = new TextField("National database URI:");
    private TextField nationalDatabaseAccessCredentials = new TextField("National database access credentials:");

    private ContactingDetails contactingDetails; // To be implemented
    private List<NaturalPerson> naturalPersons; // To be implemented

    public EODetailsForm(EODetails eoDetails) {
        this.eoDetails = eoDetails;

        setStyleName("EODetailsForm");
        setCaption("Economic operator details");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(ID);
        columnA.addComponent(electronicAddressID);
        columnA.addComponent(registrationCountryCode);
        columnA.addComponent(name);
        columnB.addComponent(role);
        columnB.addComponent(smeIndicator);
        columnB.addComponent(nationalDatabaseURI);
        columnB.addComponent(nationalDatabaseAccessCredentials);
        panelContent.addComponent(new PostalAddressForm(eoDetails.getPostalAddress()));

        for (NaturalPerson naturalPerson : this.eoDetails.getNaturalPersons()) {
            panelContent.addComponent(new NaturalPersonForm(naturalPerson));
        }

        final BeanFieldGroup<EODetails> binder = new BeanFieldGroup<>(EODetails.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.eoDetails);
        binder.setBuffered(false);

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
    }
}
