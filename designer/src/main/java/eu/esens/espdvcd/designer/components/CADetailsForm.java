package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;

import java.util.Arrays;

/**
 * Created by ixuz on 2/23/16.
 */
public class CADetailsForm extends VerticalLayout {
    private Panel panelInformationAboutPublication;
    private VerticalLayout layoutInformationAboutPublication;
    private Panel panelIdentifyProcurer;
    private VerticalLayout layoutIdentifyProcurer;
    private Panel panelInformationProcurementProcedure;
    private VerticalLayout layoutInformationProcurementProcedure;
    private Label informationAboutPublicationText = new Label("For procurement procedures in which a call for competition has been published in the Official Journal of the European Union, the information required under Part I will be automatically retrieved, provided that the electronic ESPD-service is used to generate and fill in the ESPD. Reference of the relevant notice published in the Official Journal of the European Union:");

    @PropertyId("CACountry")
    private CountryComboBox caCountry;
    @PropertyId("CAOfficialName")
    private TextField caOfficialName;
    private TextField procurementProcedureTitle;
    private TextField procurementProcedureDesc;
    private TextField procurementProcedureFileReferenceNo;
    private ProducurementPublicationNumberField procurementPublicationNumber;

    public CADetailsForm(ESPDRequest espdRequest) {
        setMargin(true);
        setSpacing(true);
        setCaption(null);
        setWidth("100%");
        setStyleName("caDetailsForm-layout");
        panelInformationAboutPublication = new Panel("Information about publication");
        layoutInformationAboutPublication = new VerticalLayout();
        panelIdentifyProcurer = new Panel("Identity of the procurer");
        layoutIdentifyProcurer = new VerticalLayout();
        panelInformationProcurementProcedure = new Panel("Information about the procurement procedure");
        layoutInformationProcurementProcedure = new VerticalLayout();
        caCountry =  new CountryComboBox("Country:", Arrays.asList("Germany", "Greece", "Sweden"));
        caOfficialName = new TextField();
        procurementProcedureTitle = new TextField();
        procurementProcedureDesc = new TextField();
        procurementProcedureFileReferenceNo = new TextField();
        procurementPublicationNumber = new ProducurementPublicationNumberField("Notice number in the OJS:");

        this.addComponent(panelInformationAboutPublication);
        panelInformationAboutPublication.setContent(layoutInformationAboutPublication);
        this.addComponent(panelIdentifyProcurer);
        panelIdentifyProcurer.setContent(layoutIdentifyProcurer);
        this.addComponent(panelInformationProcurementProcedure);
        panelInformationProcurementProcedure.setContent(layoutInformationProcurementProcedure);
        layoutInformationAboutPublication.addComponent(informationAboutPublicationText);
        layoutInformationAboutPublication.addComponent(procurementPublicationNumber);
        layoutIdentifyProcurer.addComponent(caCountry);
        layoutIdentifyProcurer.addComponent(caOfficialName);
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
        caCountry.setCaption("Country:");
        caOfficialName.setCaption("Official Name:");
        caOfficialName.setNullRepresentation(" ");
        procurementProcedureTitle.setCaption("Title:");
        procurementProcedureTitle.setNullRepresentation(" ");
        procurementProcedureDesc.setCaption("Short Description:");
        procurementProcedureDesc.setNullRepresentation(" ");
        procurementProcedureFileReferenceNo.setCaption("File reference number attributed by the contracting authority or contracting entity (if applicable):");
        procurementProcedureFileReferenceNo.setNullRepresentation(" ");

        CADetails caDetails = espdRequest.getCADetails();

        final BeanFieldGroup<CADetails> binder = new BeanFieldGroup<CADetails>(CADetails.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(caDetails);
        binder.setBuffered(false);
    }
}
