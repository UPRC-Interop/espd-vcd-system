package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;

import java.util.Arrays;

/**
 * Created by ixuz on 2/23/16.
 */
public class CADetailsForm extends VerticalLayout {
    private Panel panelIdentifyProcurer;
    private VerticalLayout layoutIdentifyProcurer;
    private Panel panelInformationProcurementProcedure;
    private VerticalLayout layoutInformationProcurementProcedure;
    @PropertyId("CACountry")
    private CountryComboBox caCountry;
    @PropertyId("CAOfficialName")
    private TextField caOfficialName;
    private TextField procurementProcedureTitle;
    private TextField procurementProcedureDesc;
    private TextField procurementProcedureFileReferenceNo;

    public CADetailsForm(ESPDRequest espdRequest) {
        setMargin(true);
        setCaption(null);
        panelIdentifyProcurer = new Panel("Identity of the procurer");
        layoutIdentifyProcurer = new VerticalLayout();
        panelInformationProcurementProcedure = new Panel("Information about the procurement procedure");
        layoutInformationProcurementProcedure = new VerticalLayout();
        caCountry =  new CountryComboBox("Country:", Arrays.asList("Germany", "Greece", "Sweden"));
        caOfficialName = new TextField();
        procurementProcedureTitle = new TextField();
        procurementProcedureDesc = new TextField();
        procurementProcedureFileReferenceNo = new TextField();

        this.addComponent(panelIdentifyProcurer);
        panelIdentifyProcurer.setContent(layoutIdentifyProcurer);
        this.addComponent(panelInformationProcurementProcedure);
        panelInformationProcurementProcedure.setContent(layoutInformationProcurementProcedure);
        layoutIdentifyProcurer.addComponent(caCountry);
        layoutIdentifyProcurer.addComponent(caOfficialName);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureTitle);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureDesc);
        layoutInformationProcurementProcedure.addComponent(procurementProcedureFileReferenceNo);

        panelIdentifyProcurer.setWidth("100%");
        layoutIdentifyProcurer.setMargin(true);
        layoutInformationProcurementProcedure.setMargin(true);
        caCountry.setCaption("Country:");
        caOfficialName.setCaption("Official Name:");
        procurementProcedureTitle.setCaption("Title:");
        procurementProcedureDesc.setCaption("Short Description:");
        procurementProcedureFileReferenceNo.setCaption("File reference number attributed by the contracting authority or contracting entity (if applicable):");

        CADetails caDetails = new CADetails();
        caDetails.setCACountry("Sweden");
        caDetails.setCAOfficialName("");
        caDetails.setProcurementProcedureTitle("");
        caDetails.setProcurementProcedureDesc("");
        caDetails.setProcurementProcedureFileReferenceNo("");

        final BeanFieldGroup<CADetails> binder = new BeanFieldGroup<CADetails>(CADetails.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(caDetails);
        espdRequest.setCADetails(caDetails);
        binder.setBuffered(false);
    }
}
