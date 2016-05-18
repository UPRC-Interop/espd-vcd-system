package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.NaturalPerson;

public class NaturalPersonForm extends Panel {

    private NaturalPerson naturalPerson = null;
    private VerticalLayout panelContent = new VerticalLayout();
    private HorizontalLayout columns = new HorizontalLayout();
    private FormLayout columnA = new FormLayout();
    private FormLayout columnB = new FormLayout();
    private TextField role = new TextField("Role:");
    private TextField birthPlace = new TextField("Birth place:");
    private DateField birthDate = new DateField("Birth date:");
    private TextArea powerOfAttorney = new TextArea("Detailed information:");

    public NaturalPersonForm(NaturalPerson naturalPerson, boolean readOnly) {
        this.naturalPerson = naturalPerson;

        setStyleName("NaturalPersonForm");
        setCaption("Representative");
        setContent(panelContent);
        setWidth("100%");

        panelContent.addComponent(columns);
        columns.addComponent(columnA);
        columns.addComponent(columnB);

        columnA.addComponent(role);
        columnA.addComponent(birthPlace);
        columnA.addComponent(birthDate);
        columnB.addComponent(powerOfAttorney);
        panelContent.addComponent(new PostalAddressForm(this.naturalPerson.getPostalAddress(), readOnly));

        final BeanFieldGroup<NaturalPerson> binder = new BeanFieldGroup<>(NaturalPerson.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.naturalPerson);
        binder.setBuffered(false);
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

        role.setNullRepresentation("");
        role.setInputPrompt("Role");
        role.setWidth(300, Unit.PIXELS);

        birthPlace.setNullRepresentation("");
        birthPlace.setInputPrompt("Birth place");
        birthPlace.setWidth(300, Unit.PIXELS);

        birthDate.setWidth(300, Unit.PIXELS);
        birthDate.setDateFormat("dd/MM/yyyy");

        powerOfAttorney.setNullRepresentation("");
        powerOfAttorney.setInputPrompt("Detailed information");
        powerOfAttorney.setWidth(300, Unit.PIXELS);
    }
}
