package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ModelBuilder;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.components.ESPDResponseForm;
import eu.esens.espdvcd.model.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Espd extends Master {
    private HorizontalLayout panels = null;
    private ESPDResponse espdResponse = null;
    private ESPDResponseForm espdResponseForm = null;
    VerticalLayout panelRightLayout = new VerticalLayout();
    Button panelRightButtonImport = new Button("Import ESPD Artifact");
    private CriterionForm highlightedCriterion = null;
    private Panel uploadPanel = new Panel();

    public Espd(Navigator navigator) {
        super(navigator, true);

        panels = new HorizontalLayout();
        panels.setStyleName("EspdTemplate-panels");
        panels.setWidth("100%");
        mainContent.addComponent(panels);

        Panel panelRight = new Panel("Create a Response by Importing an existing ESPD Artifact");
        panels.addComponent(panelRight);

        panelRightLayout.setStyleName("panelRightLayout");
        panelRight.setContent(panelRightLayout);

        Label panelRightDescription = new Label("For creating an ESPD Response, you must first import"
                + " an ESPD XML Artifact (either an ESPD Request or an ESPD Response). "
                + "The Criteria and Procurement details will be imported from an ESPD Request. "
                + "When importing an ESPD Response, the Critera, Procurement and Economic Operator Details"
                + "Will be imported");
        
        panelRightLayout.addComponent(panelRightDescription);

        panelRightButtonImport.addClickListener(this::onImportEspdTemplate);
        panelRightButtonImport.setStyleName("espdTemplate-panelButton");
        panelRightLayout.addComponent(panelRightButtonImport);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    public void onImportEspdTemplate(Button.ClickEvent clickEvent) {
        Espd thisView = this;
        panelRightButtonImport.setVisible(false);

        class EspdUploader2 implements Upload.Receiver, Upload.SucceededListener {
            public File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                FileOutputStream fos = null; // Stream to write to
                try {
                    file = File.createTempFile("espd", ".xml");
                    fos = new FileOutputStream(file);
                } catch (final java.io.IOException e) {
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
                    espdResponse = new ModelBuilder().importFrom(is).createESPDResponse();

                    if (espdResponse.getEODetails() == null) {
                        EODetails eoDetails = new EODetails();
                        eoDetails.setContactingDetails(new ContactingDetails());
                        eoDetails.setPostalAddress(new PostalAddress());
                        espdResponse.setEODetails(eoDetails);
                    }

                    espdResponseForm = new ESPDResponseForm(thisView, espdResponse, 0, false);
                    mainContent.addComponent(espdResponseForm);
                    is.close();
                    panels.setVisible(false);
                } catch (IOException e) {
                    Notification.show("Please ensure that the file is a valid ESPD Request or ESPD Response",
                            "Import failed",
                            Notification.Type.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (Exception e) {
                    Notification.show("Please ensure that the file is a valid ESPD Request or ESPD Response",
                            "Import failed",
                            Notification.Type.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };

        EspdUploader2 receiver = new EspdUploader2();
        Upload upload = new Upload(null, receiver);
        upload.setButtonCaption("Import");
        upload.addSucceededListener(receiver);

        VerticalLayout uploadPanelLayout = new VerticalLayout();
        uploadPanelLayout.setMargin(true);
        uploadPanelLayout.addComponent(upload);
        uploadPanel.setContent(uploadPanelLayout);
        panelRightLayout.addComponent(uploadPanel);
    }

    public CriterionForm getHighlightedCriterion() {
        return highlightedCriterion;
    }

    public void setHighlightedCriterion(CriterionForm highlightedCriterion) {
        this.highlightedCriterion = highlightedCriterion;
    }

    public void resetView() {
        panels.setVisible(true);
        panelRightButtonImport.setVisible(true);
        mainContent.removeComponent(espdResponseForm);
        panelRightLayout.removeComponent(uploadPanel);
    }
}
