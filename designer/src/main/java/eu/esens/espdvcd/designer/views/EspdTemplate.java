package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SimpleESPDRequest;

import java.io.*;

/**
 * Created by ixuz on 3/4/16.
 */
public class EspdTemplate extends Master {

    private HorizontalLayout panels = null;
    private ESPDRequest espdRequest = null;
    private ESPDRequestForm espdRequestForm = null;
    VerticalLayout panelLeftLayout = new VerticalLayout();
    VerticalLayout panelRightLayout = new VerticalLayout();
    Button panelLeftButtonNew = new Button("Create new ESPD Template");
    Button panelRightButtonImport = new Button("Import existing ESPD Template");

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

        panelLeftLayout.setStyleName("panelLeftLayout");
        panelLeft.setContent(panelLeftLayout);

        Label panelLeftDescription = new Label("A description about creating a new espd template. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
        panelLeftLayout.addComponent(panelLeftDescription);

        panelLeftButtonNew.setStyleName("espdTemplate-panelButton");
        panelLeftButtonNew.addClickListener(this::onNewEspdTemplate);
        panelLeftLayout.addComponent(panelLeftButtonNew);

        Panel panelRight = new Panel("Import existing ESPD Template");
        panelRight.setStyleName("EspdTemplate-panelRight");
        panelRight.setWidth("50%");
        panels.addComponent(panelRight);

        panelRightLayout.setStyleName("panelRightLayout");
        panelRight.setContent(panelRightLayout);

        Label panelRightDescription = new Label("A description about importing an existing espd template. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
        panelRightLayout.addComponent(panelRightDescription);

        panelRightButtonImport.addClickListener(this::onImportEspdTemplate);
        panelRightButtonImport.setStyleName("espdTemplate-panelButton");
        panelRightLayout.addComponent(panelRightButtonImport);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    public void onNewEspdTemplate(Button.ClickEvent clickEvent) {
        panels.setVisible(false);

        CADetails caDetails = new CADetails();
        caDetails.setCACountry("Sweden");

        espdRequest = new SimpleESPDRequest();
        espdRequest.setCADetails(caDetails);
        espdRequest.setCriterionList(new ESPDBuilder().getCriteriaList());

        // Cenerate the espd request form base on the provided espd request model
        espdRequestForm = new ESPDRequestForm(this, espdRequest);
        mainContent.addComponent(espdRequestForm);
    }

    public void onImportEspdTemplate(Button.ClickEvent clickEvent) {
        EspdTemplate thisView = this;
        panelRightButtonImport.setVisible(false);

        class EspdUploader2 implements Upload.Receiver, Upload.SucceededListener {
            public File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                FileOutputStream fos = null; // Stream to write to
                try {
                    file = new File("tmp/uploaded.xml");
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos;
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {

                try {
                    InputStream is = new FileInputStream(file);
                    ESPDBuilder espdBuilder = new ESPDBuilder();
                    espdRequest = espdBuilder.createFromXML(is);
                    espdRequestForm = new ESPDRequestForm(thisView, espdRequest);
                    mainContent.addComponent(espdRequestForm);
                    is.close();
                    panels.setVisible(false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        EspdUploader2 receiver = new EspdUploader2();
        Upload upload = new Upload(null, receiver);
        upload.setButtonCaption("Import");
        upload.addSucceededListener(receiver);

        Panel uploadPanel = new Panel();
        VerticalLayout uploadPanelLayout = new VerticalLayout();
        uploadPanelLayout.setMargin(true);
        uploadPanelLayout.addComponent(upload);
        uploadPanel.setContent(uploadPanelLayout);
        panelRightLayout.addComponent(uploadPanel);
    }
}
