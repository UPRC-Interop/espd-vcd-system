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
import eu.esens.espdvcd.model.ESPDRequest;

public class CADetailsForm extends VerticalLayout {
    private Panel panelInformationAboutPublication;
    private VerticalLayout layoutInformationAboutPublication;
    private FormLayout layoutFormInformatinAboutPublication;
    private Panel panelIdentifyProcurer;
    private VerticalLayout layoutIdentifyProcurer;
    private FormLayout layoutIdentifyProcurerOfficialName;
    private FormLayout layoutIdentifyProcurerCountry;
    private Panel panelInformationProcurementProcedure;
    private FormLayout layoutInformationProcurementProcedure;
    private Label informationAboutPublicationText = new Label("For procurement procedures in which a call "
            + "for competition has been published in the Official Journal of the European Union, the information "
            + "required under Part I will be automatically retrieved, provided that the electronic ESPD-service "
            + "is used to generate and fill in the ESPD. Reference of the relevant notice published in the "
            + "Official Journal of the European Union:");

    @PropertyId("CACountry")
    private CountryComboBox caCountry;
    @PropertyId("CAOfficialName")
    private TextField caOfficialName;
    private TextField procurementProcedureTitle;
    private TextArea procurementProcedureDesc;
    private TextField procurementProcedureFileReferenceNo;

    
    
    //Optionals, part of Core Vocabulary
    private TextField websiteURI;
    private TextField endpointId;

    private ContactingDetailsForm caContactingDetailsForm;
    
    private ProducurementPublicationNumberField procurementPublicationNumber;

    public CADetailsForm(ESPDRequest espdRequest, boolean readOnly) {
        setMargin(true);
        setSpacing(true);
        setCaption(null);
        setWidth("100%");
        setStyleName("caDetailsForm-layout");
        
        CADetails caDetails = espdRequest.getCADetails();
                
        panelInformationAboutPublication = new Panel("Information about publication");
        layoutInformationAboutPublication = new VerticalLayout();
        panelIdentifyProcurer = new Panel("Identity of the procurer");
        
        layoutIdentifyProcurer = new VerticalLayout();
        layoutIdentifyProcurerOfficialName = new FormLayout();
        layoutIdentifyProcurerCountry = new FormLayout();

        layoutFormInformatinAboutPublication=new FormLayout();

        panelInformationProcurementProcedure = new Panel("Information about the procurement procedure");
        layoutInformationProcurementProcedure = new FormLayout();
        caCountry =  new CountryComboBox("Country:");
        caOfficialName = new TextField();
        procurementProcedureTitle = new TextField();
        procurementProcedureDesc = new TextArea();
        procurementProcedureFileReferenceNo = new TextField();
        procurementPublicationNumber = new ProducurementPublicationNumberField("Notice number in the OJS:");

        this.addComponent(panelInformationAboutPublication);
        panelInformationAboutPublication.setContent(layoutInformationAboutPublication);
        this.addComponent(panelIdentifyProcurer);
        panelIdentifyProcurer.setContent(layoutIdentifyProcurer);
        this.addComponent(panelInformationProcurementProcedure);
        panelInformationProcurementProcedure.setContent(layoutInformationProcurementProcedure);
        layoutInformationAboutPublication.addComponent(informationAboutPublicationText);
        layoutFormInformatinAboutPublication.addComponent(procurementPublicationNumber);
        layoutInformationAboutPublication.addComponent(layoutFormInformatinAboutPublication);
        
        HorizontalLayout horLayoutIdentifyProcurer = new HorizontalLayout();
        
        
        horLayoutIdentifyProcurer.addComponent(layoutIdentifyProcurerOfficialName);
        horLayoutIdentifyProcurer.addComponent(layoutIdentifyProcurerCountry);
        
        layoutIdentifyProcurer.addComponent(horLayoutIdentifyProcurer);
        layoutIdentifyProcurerOfficialName.addComponent(caOfficialName);
        layoutIdentifyProcurerCountry.addComponent(caCountry);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureTitle);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureDesc);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureFileReferenceNo);

        panelInformationAboutPublication.setWidth("100%");
        panelIdentifyProcurer.setWidth("100%");
        layoutInformationAboutPublication.setWidth(100, Unit.PERCENTAGE);
        layoutInformationAboutPublication.setMargin(true);
        layoutIdentifyProcurer.setMargin(true);
        layoutInformationProcurementProcedure.setMargin(true);
        layoutInformationProcurementProcedure.setWidthUndefined();
        informationAboutPublicationText.setWidth(100, Unit.PERCENTAGE);
        caCountry.setCaption("Country:");
        caCountry.setStyleName("textFieldCaptionText");
        caOfficialName.setCaption("Official Name:");
        caOfficialName.setWidth("17em");
        caOfficialName.setStyleName("textFieldCaptionText");

        procurementProcedureTitle.setCaption("Title:");
        //procurementProcedureTitle.setInputPrompt("Enter title");
        procurementProcedureTitle.setWidth("20em");
        procurementProcedureTitle.setStyleName("textFieldCaptionText");
        procurementProcedureDesc.setCaption("Short Description:");
        //procurementProcedureDesc.setInputPrompt("Enter short description");
        procurementProcedureDesc.setWidth("20em");
        procurementProcedureDesc.setStyleName("textFieldCaptionText");
        procurementProcedureFileReferenceNo.setCaption("File reference number attributed by the contracting authority or contracting entity (if applicable):");
        procurementProcedureFileReferenceNo.setStyleName("textCaption");
        //procurementProcedureFileReferenceNo.setInputPrompt("Enter file reference number");
        procurementProcedureFileReferenceNo.setWidth("20em");
        procurementProcedureFileReferenceNo.setStyleName("textFieldCaptionText");
        procurementPublicationNumber.setWidth("30em");
        procurementPublicationNumber.setStyleName("textFieldCaptionText");
        
        caContactingDetailsForm = new ContactingDetailsForm(caDetails.getContactingDetails(), readOnly, true);
        layoutIdentifyProcurer.addComponent(caContactingDetailsForm);
             

        final Binder<CADetails> binder = new BeanValidationBinder<>(CADetails.class);
        binder.bindInstanceFields(this);
        binder.setBean(caDetails);
        binder.setReadOnly(readOnly);
    }
}
