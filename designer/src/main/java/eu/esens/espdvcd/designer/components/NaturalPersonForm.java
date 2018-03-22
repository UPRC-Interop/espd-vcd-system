package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.NaturalPerson;

public class NaturalPersonForm extends Panel {

    private NaturalPerson naturalPerson = null;
    private VerticalLayout panelContent = new VerticalLayout();
    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();
    private TextField firstName = new TextField("First name:");
    private TextField familyName = new TextField("Family name:");
    private TextField role = new TextField("Role:");
    private TextField birthPlace = new TextField("Birth place:");
    private DateField birthDate = new DateField("Birth date:");
    //private TextArea powerOfAttorney = new TextArea("Detailed information:");

    public NaturalPersonForm(NaturalPerson naturalPerson, boolean readOnly) {
        this.naturalPerson = naturalPerson;

        setStyleName("NaturalPersonForm");
        setCaption("Representative");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        /*columnA.addComponent(firstName);
        columnA.addComponent(role);
        columnA.addComponent(birthPlace);
        columnA.addComponent(birthDate);
        columnB.addComponent(familyName);
        columnB.addComponent(powerOfAttorney);*/

        columnA.addComponent(firstName);
        columnA.addComponent(birthPlace);
        columnA.addComponent(role);
        columnB.addComponent(familyName);
        columnB.addComponent(birthDate);


        if (this.naturalPerson.getPostalAddress() != null) {
            panelContent.addComponent(new PostalAddressForm(this.naturalPerson.getPostalAddress(), readOnly));
        }
        if (this.naturalPerson.getContactDetails() != null) {
            panelContent.addComponent(new ContactingDetailsForm(this.naturalPerson.getContactDetails(), readOnly, false, false));
        }

        final Binder<NaturalPerson> binder = new BeanValidationBinder<>(NaturalPerson.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.naturalPerson);
        binder.setReadOnly(readOnly);

        setWidth("100%");
        panelContent.setStyleName("NaturalPersonFormPanelContent");
        panelContent.setSpacing(true);
        panelContent.setMargin(true);
        columns.setWidth("100%");
        columns.setSpacing(true);
        columns.setMargin(false);
        columnA.setSpacing(true);
        columnA.setMargin(false);
        columnB.setSpacing(true);
        columnB.setMargin(false);

        firstName.setPlaceholder("First name");
        firstName.setWidth(300, Unit.PIXELS);

        familyName.setPlaceholder("Family name");
        familyName.setWidth(300, Unit.PIXELS);

        role.setPlaceholder("Role");
        role.setWidth(300, Unit.PIXELS);

        birthPlace.setPlaceholder("Birth place");
        birthPlace.setWidth(300, Unit.PIXELS);

        birthDate.setWidth(300, Unit.PIXELS);
        birthDate.setDateFormat("dd/MM/yyyy");

        //powerOfAttorney.setPlaceholder("Detailed information");
        //powerOfAttorney.setWidth(300, Unit.PIXELS);
    }
}
