package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;

import java.util.Date;

/**
 * Created by ixuz on 4/13/16.
 */
public class NaturalPersonForm extends Panel {

    private NaturalPerson naturalPerson = null;

    private VerticalLayout panelContent = new VerticalLayout();

    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();

    private TextField name = new TextField("Name:");
    private TextField ID = new TextField("ID:");
    private TextField role = new TextField("Role:");
    private TextField birthPlace = new TextField("Birth place:");
    private DateField birthDate = new DateField("Birth date:");
    private CountryComboBox registrationCountry = new CountryComboBox("Registration country:");
    private TextField powerOfAttorney = new TextField("Power of attourney:");
    //private PostalAddressForm postalAddress = new TextField("Name:");

    public NaturalPersonForm(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;

        setStyleName("NaturalPersonForm");
        setCaption("Natural person form");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(name);
        columnA.addComponent(ID);
        columnA.addComponent(role);
        columnA.addComponent(birthPlace);
        columnB.addComponent(birthDate);
        columnB.addComponent(registrationCountry);
        columnB.addComponent(powerOfAttorney);
        panelContent.addComponent(new PostalAddressForm(this.naturalPerson.getPostalAddress()));

        final BeanFieldGroup<NaturalPerson> binder = new BeanFieldGroup<>(NaturalPerson.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.naturalPerson);
        binder.setBuffered(false);

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

        name.setNullRepresentation("");
        name.setInputPrompt("Name");
        name.setWidth(300, Unit.PIXELS);

        ID.setNullRepresentation("");
        ID.setInputPrompt("ID");
        ID.setWidth(300, Unit.PIXELS);

        role.setNullRepresentation("");
        role.setInputPrompt("Role");
        role.setWidth(300, Unit.PIXELS);

        birthPlace.setNullRepresentation("");
        birthPlace.setInputPrompt("Birth place");
        birthPlace.setWidth(300, Unit.PIXELS);

        birthDate.setWidth(300, Unit.PIXELS);

        registrationCountry.setInputPrompt("Registration Country");
        registrationCountry.setWidth(300, Unit.PIXELS);

        powerOfAttorney.setNullRepresentation("");
        powerOfAttorney.setInputPrompt("Power of attourney");
        powerOfAttorney.setWidth(300, Unit.PIXELS);
    }
}
