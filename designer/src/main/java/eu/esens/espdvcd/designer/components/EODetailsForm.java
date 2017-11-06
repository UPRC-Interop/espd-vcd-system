package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;
import java.util.Arrays;
import java.util.List;

public final class EODetailsForm extends Panel {

    EODetails eoDetails = null;

    private VerticalLayout panelContent = new VerticalLayout();

    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();

    private TextField ID = new TextField("ID:");
    private TextField electronicAddressID = new TextField("Electronic address id:");
    private TextField name = new TextField("Name:");
    private TextField role = new TextField("Role:");
    private TextField procurementProjectLot = new TextField("Where applicable, indication of the lot(s) for which the economic operator wishes to tender:");
    private TextField websiteURI = new TextField("Web site address: ");
    private TextField endpoindId = new TextField("Endpoint ID: ");
    
    private RadioButtonGroup<Boolean> smeIndicator = new RadioButtonGroup("Indicator: ");
   
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
        columnA.addComponent(websiteURI);
        columnA.addComponent(endpoindId);
        
        columnA.setMargin(true);
        columnA.setWidthUndefined();
        
        columnB.addComponent(role);
        columnB.addComponent(smeIndicator);
        
        columnB.setMargin(true);
        columnB.setWidthUndefined();

        smeIndicator.setCaption("Is the economic operator a Micro, a Small or a Medium-Sized Enterprise:");
        smeIndicator.setStyleName("horizontal textFieldCaptionText");
        smeIndicator.setWidth("20em");
        smeIndicator.setItems(Arrays.asList(true,false));
        smeIndicator.setItemCaptionGenerator(item -> (item == true) ? "Yes" : "No" );
        
        procurementProjectLot.setWidth("20em");
        procurementProjectLot.setStyleName("textFieldCaptionText");
        
        columnB.addComponent(smeIndicator);       
        columnB.addComponent(procurementProjectLot);


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

        final Binder<EODetails> binder = new BeanValidationBinder<>(EODetails.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.eoDetails);
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

        ID.setPlaceholder("Please enter ID");
        ID.setWidth(300, Unit.PIXELS);

        electronicAddressID.setPlaceholder("Electronic address ID");
        electronicAddressID.setWidth(300, Unit.PIXELS);

        name.setPlaceholder("Name");
        name.setWidth(300, Unit.PIXELS);

        role.setPlaceholder("Role");
        role.setWidth(400, Unit.PIXELS);
    }
}
