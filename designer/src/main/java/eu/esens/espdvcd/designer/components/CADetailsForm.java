package eu.esens.espdvcd.designer.components;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.PostalAddress;

public class CADetailsForm extends VerticalLayout {
    private Panel panelInformationAboutPublication;
    private VerticalLayout layoutInformationAboutPublication;
    private FormLayout layoutFormInformatinAboutPublication;
    private Panel panelIdentifyProcurer;
    private VerticalLayout layoutIdentifyProcurer;
    private FormLayout layoutIdentifyProcurerOfficialName;
    //private FormLayout layoutIdentifyProcurerCountry;
    private Panel panelInformationProcurementProcedure;
    private FormLayout layoutInformationProcurementProcedure;
    private Label informationAboutPublicationText = new Label("For procurement procedures in which a call for competition has been published in the Official Journal of the European Union, the information required under Part I will be automatically retrieved, provided that the electronic ESPD-service is used to generate and fill in the ESPD. Reference of the relevant notice published in the Official Journal of the European Union:");

    //@PropertyId("CACountry")
    //private CountryComboBox caCountry;
    @PropertyId("CAOfficialName")
    private TextField caOfficialName;
    private TextField ID;
    private TextField webSiteURI;
    private TextField procurementProcedureTitle;
    private TextArea procurementProcedureDesc;
    private TextField procurementProcedureFileReferenceNo;
    private ProducurementPublicationNumberField procurementPublicationNumber;
    private TextField procurementPublicationURI;

    public CADetailsForm(ESPDRequest espdRequest, boolean readOnly) {
        setMargin(true);
        setSpacing(true);
        setCaption(null);
        setWidth("100%");
        setStyleName("caDetailsForm-layout");
        panelInformationAboutPublication = new Panel("Information about publication");
        layoutInformationAboutPublication = new VerticalLayout();
        panelIdentifyProcurer = new Panel("Identity of the procurer");
        layoutIdentifyProcurer = new VerticalLayout();
        layoutIdentifyProcurerOfficialName = new FormLayout();
        //layoutIdentifyProcurerCountry = new FormLayout();

        layoutFormInformatinAboutPublication=new FormLayout();

        CADetails caDetails = espdRequest.getCADetails();

        //caCountry =  new CountryComboBox("Country:");
        caOfficialName = new TextField();
        layoutIdentifyProcurerOfficialName.addComponent(caOfficialName);

        ID = new TextField();
        layoutIdentifyProcurerOfficialName.addComponent(ID);

        webSiteURI = new TextField();
        layoutIdentifyProcurerOfficialName.addComponent(webSiteURI);
        //layoutIdentifyProcurerCountry.addComponent(caCountry);
        layoutIdentifyProcurer.addComponent(layoutIdentifyProcurerOfficialName);
        //layoutIdentifyProcurer.addComponent(layoutIdentifyProcurerCountry);

        PostalAddress caDetailsPostalAddress = caDetails.getPostalAddress();
        if (caDetailsPostalAddress != null) {
            layoutIdentifyProcurer.addComponent(new PostalAddressForm(caDetailsPostalAddress, readOnly));
        }

        ContactingDetails caDetailsContactingDetails = caDetails.getContactingDetails();
        if (caDetailsContactingDetails != null) {
            layoutIdentifyProcurer.addComponent(new ContactingDetailsForm(caDetailsContactingDetails, readOnly, true));
        }

        panelInformationProcurementProcedure = new Panel("Information about the procurement procedure");
        layoutInformationProcurementProcedure = new FormLayout();

        procurementProcedureTitle = new TextField();
        procurementProcedureDesc = new TextArea();
        procurementProcedureFileReferenceNo = new TextField();
        procurementPublicationNumber = new ProducurementPublicationNumberField("Notice number in the OJS:");
        procurementPublicationURI = new TextField();

        this.addComponent(panelInformationAboutPublication);
        panelInformationAboutPublication.setContent(layoutInformationAboutPublication);
        this.addComponent(panelIdentifyProcurer);
        panelIdentifyProcurer.setContent(layoutIdentifyProcurer);
        this.addComponent(panelInformationProcurementProcedure);
        panelInformationProcurementProcedure.setContent(layoutInformationProcurementProcedure);
        layoutInformationAboutPublication.addComponent(informationAboutPublicationText);
        layoutFormInformatinAboutPublication.addComponent(procurementPublicationNumber);
        layoutFormInformatinAboutPublication.addComponent(procurementPublicationURI);
        layoutInformationAboutPublication.addComponent(layoutFormInformatinAboutPublication);

        layoutInformationProcurementProcedure.addComponent(procurementProcedureTitle);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureDesc);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureFileReferenceNo);

        panelInformationAboutPublication.setWidth("100%");
        panelIdentifyProcurer.setWidth("100%");
        layoutInformationAboutPublication.setWidth(100, Unit.PERCENTAGE);
        layoutInformationAboutPublication.setMargin(true);
        layoutIdentifyProcurer.setMargin(true);
        layoutInformationProcurementProcedure.setMargin(true);
        informationAboutPublicationText.setWidth(100, Unit.PERCENTAGE);
        //caCountry.setCaption("Country:");
        //caCountry.setStyleName("textFieldCaptionText");
        caOfficialName.setCaption("Official Name:");
        //caOfficialName.setInputPrompt("Enter official name");
        caOfficialName.setWidth("30em");
        caOfficialName.setStyleName("textFieldCaptionText");

        ID.setCaption("ID:");
        ID.setWidth("30em");
        ID.setStyleName("textFieldCaptionText");

        webSiteURI.setCaption("Internet address (web address):");
        webSiteURI.setWidth("30em");
        webSiteURI.setStyleName("textFieldCaptionText");

        procurementProcedureTitle.setCaption("Title:");
        //procurementProcedureTitle.setInputPrompt("Enter title");
        procurementProcedureTitle.setWidth("30em");
        procurementProcedureTitle.setStyleName("textFieldCaptionText");
        procurementProcedureDesc.setCaption("Short Description:");
        //procurementProcedureDesc.setInputPrompt("Enter short description");
        procurementProcedureDesc.setWidth("30em");
        procurementProcedureDesc.setStyleName("textFieldCaptionText");
        procurementProcedureFileReferenceNo.setCaption("File reference number attributed by the contracting authority or contracting entity (if applicable):");
        procurementProcedureFileReferenceNo.setStyleName("textCaption");
        //procurementProcedureFileReferenceNo.setInputPrompt("Enter file reference number");
        procurementProcedureFileReferenceNo.setWidth("30em");
        procurementProcedureFileReferenceNo.setStyleName("textFieldCaptionText");
        procurementPublicationNumber.setWidth("30em");
        procurementPublicationNumber.setStyleName("textFieldCaptionText");
        procurementPublicationURI.setCaption("OJS URL:");
        procurementPublicationURI.setWidth("30em");
        procurementPublicationURI.setStyleName("textFieldCaptionText");

        final Binder<CADetails> binder = new BeanValidationBinder<>(CADetails.class);
        binder.bindInstanceFields(this);
        binder.setBean(caDetails);
        binder.setReadOnly(readOnly);
    }
}
