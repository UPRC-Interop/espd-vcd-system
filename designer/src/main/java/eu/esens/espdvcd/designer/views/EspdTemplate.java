package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.Designer;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.designer.components.LegislationReferenceForm;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SimpleESPDRequest;

/**
 * Created by ixuz on 3/4/16.
 */
public class EspdTemplate extends Master {

    private HorizontalLayout panels = null;
    private ESPDRequest espdRequest = null;
    private ESPDRequestForm espdRequestForm = null;

    public EspdTemplate(Navigator navigator) {
        super(navigator, true);

        panels = new HorizontalLayout();
        panels.setStyleName("EspdTemplate-panels");
        panels.setWidth("100%");
        mainContent.addComponent(panels);

        Panel panelLeft = new Panel("Create new ESPD Template");
        panelLeft.setStyleName("EspdTemplate-panelLeft");
        panelLeft.setWidth("50%");
        panels.addComponent(panelLeft);

        VerticalLayout panelLeftLayout = new VerticalLayout();
        panelLeftLayout.setStyleName("panelLeftLayout");
        panelLeft.setContent(panelLeftLayout);

        Label panelLeftDescription = new Label("A description about creating a new espd template. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
        panelLeftLayout.addComponent(panelLeftDescription);

        Button panelLeftButtonNew = new Button("Create new ESPD Template");
        panelLeftButtonNew.setStyleName("espdTemplate-panelButton");
        panelLeftButtonNew.addClickListener(this::onNewEspdTemplate);
        panelLeftLayout.addComponent(panelLeftButtonNew);

        Panel panelRight = new Panel("Import existing ESPD Template");
        panelRight.setStyleName("EspdTemplate-panelRight");
        panelRight.setWidth("50%");
        panels.addComponent(panelRight);

        VerticalLayout panelRightLayout = new VerticalLayout();
        panelRightLayout.setStyleName("panelRightLayout");
        panelRight.setContent(panelRightLayout);

        Label panelRightDescription = new Label("A description about importing an existing espd template. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
        panelRightLayout.addComponent(panelRightDescription);

        Button panelRightButtonImport = new Button("Import existing ESPD Template");
        panelRightButtonImport.addClickListener(this::onImportEspdTemplate);
        panelRightButtonImport.setStyleName("espdTemplate-panelButton");
        panelRightLayout.addComponent(panelRightButtonImport);


        CADetails caDetails = new CADetails();
        caDetails.setCACountry("Sweden");

        espdRequest = new SimpleESPDRequest();
        espdRequest.setCADetails(caDetails);
        espdRequest.setCriterionList(new ESPDBuilder().getCriteriaList());

        // Cenerate the espd request form base on the provided espd request model
        espdRequestForm = new ESPDRequestForm(this, espdRequest);
        mainContent.addComponent(espdRequestForm);
        espdRequestForm.setVisible(false);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    public void onNewEspdTemplate(Button.ClickEvent clickEvent) {
        espdRequestForm.setVisible(true);
        panels.setVisible(false);
    }

    public void onImportEspdTemplate(Button.ClickEvent clickEvent) {
        espdRequestForm.setVisible(true);
        panels.setVisible(false);
    }
}
