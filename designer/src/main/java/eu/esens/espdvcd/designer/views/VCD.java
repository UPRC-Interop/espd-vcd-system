package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ModelBuilder;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.designer.components.ESPDResponseForm;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VCD extends Master {

    private HorizontalLayout panels = null;
    private ESPDRequest espdRequest = null;
    private ESPDRequestForm espdRequestForm = null;
    private ESPDResponse espdResponse = null;
    private ESPDResponseForm espdResponseForm = null;
    VerticalLayout panelLeftLayout = new VerticalLayout();
    VerticalLayout panelRightLayout = new VerticalLayout();
    Button panelLeftButtonImport = new Button("View existing ESPD Template");
    Button panelRightButtonImport = new Button("View existing ESPD");
    private CriterionForm highlightedCriterion = null;
    private Panel uploadPanelLeft = new Panel();
    private Panel uploadPanelRight = new Panel();
    GridLayout gridLayout = new GridLayout(2,1);
    VerticalLayout mainColumn = new VerticalLayout();

    protected Panel detailsPanel = new Panel();
    protected VerticalLayout detailsContent = new VerticalLayout();

    public VCD(Navigator navigator) {
        super(navigator, true);

        detailsPanel.setStyleName("detailsPanel");
        detailsPanel.setSizeFull();
        detailsPanel.setContent(detailsContent);
        detailsPanel.getContent().setSizeUndefined();

        detailsContent.setHeight("100%");
        detailsContent.setStyleName("master-detailsContent");
        detailsContent.setWidth("100%");

        mainContent.setWidth("100%");
        gridLayout.setWidth("100%");
        gridLayout.setHeight("100%");
        gridLayout.setColumnExpandRatio(0, 1.0f);
        gridLayout.setColumnExpandRatio(1, 0.0f);

        gridLayout.addComponent(mainColumn, 0, 0);
        gridLayout.addComponent(detailsPanel, 1, 0);
        mainContent.addComponent(gridLayout);

        panels = new HorizontalLayout();
        panels.setStyleName("EspdTemplate-panels");
        panels.setWidth("100%");
        mainColumn.addComponent(panels);

        Panel panelLeft = new Panel("Inspect an ESPD Template");
        panelLeft.setStyleName("EspdTemplate-panelLeft");
        panels.addComponent(panelLeft);

        panelLeftLayout.setStyleName("panelLeftLayout");
        panelLeft.setContent(panelLeftLayout);

        Label panelLeftDescription = new Label("A description about viewing a existing espd template. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
        panelLeftLayout.addComponent(panelLeftDescription);

        panelLeftButtonImport.setStyleName("espdTemplate-panelButton");
        panelLeftButtonImport.addClickListener(this::onImportEspdTemplate);
        panelLeftLayout.addComponent(panelLeftButtonImport);

        Panel panelRight = new Panel("Inspect an ESPD");
        panelRight.setStyleName("EspdTemplate-panelRight");
        panels.addComponent(panelRight);

        panelRightLayout.setStyleName("panelRightLayout");
        panelRight.setContent(panelRightLayout);

        Label panelRightDescription = new Label("A description about viewing an existing espd. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
        panelRightLayout.addComponent(panelRightDescription);

        panelRightButtonImport.addClickListener(this::onImportEspd);
        panelRightButtonImport.setStyleName("espdTemplate-panelButton");
        panelRightLayout.addComponent(panelRightButtonImport);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    public void onNewEspdTemplate(Button.ClickEvent clickEvent) {
        try {
            panels.setVisible(false);

            espdRequest = new ModelBuilder().addDefaultESPDCriteriaList().createESPDRequest();

            // Cenerate the espd request form base on the provided espd request model
            espdRequestForm = new ESPDRequestForm(this, espdRequest, true);
            mainColumn.addComponent(espdRequestForm);
        } catch (BuilderException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onImportEspdTemplate(Button.ClickEvent clickEvent) {
        VCD thisView = this;
        panelLeftButtonImport.setVisible(false);

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
                    espdRequest = new ModelBuilder().importFrom(is).createESPDRequest();
                    espdRequestForm = new ESPDRequestForm(thisView, espdRequest, true);
                    mainColumn.addComponent(espdRequestForm);
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
        uploadPanelLeft.setContent(uploadPanelLayout);
        panelLeftLayout.addComponent(uploadPanelLeft);
    }


    public void onImportEspd(Button.ClickEvent clickEvent) {
        VCD thisView = this;
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
                    espdResponse = new ModelBuilder().importFrom(is).createESPDResponse();
                    espdResponseForm = new ESPDResponseForm(thisView, espdResponse, true);
                    mainColumn.addComponent(espdResponseForm);
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
        uploadPanelRight.setContent(uploadPanelLayout);
        panelRightLayout.addComponent(uploadPanelRight);
    }

    public CriterionForm getHighlightedCriterion() {
        return highlightedCriterion;
    }

    public void setHighlightedCriterion(CriterionForm highlightedCriterion) {
        this.highlightedCriterion = highlightedCriterion;
    }

    public void resetView() {
        panels.setVisible(true);
        panelLeftButtonImport.setVisible(true);
        panelRightButtonImport.setVisible(true);
        gridLayout.removeComponent(espdRequestForm);
        gridLayout.removeComponent(espdResponseForm);
        panelLeftLayout.removeComponent(uploadPanelLeft);
        panelRightLayout.removeComponent(uploadPanelRight);
        getDetailsContent().removeAllComponents();
        if (espdRequestForm != null) {
            mainColumn.removeComponent(espdRequestForm);
            espdRequestForm = null;
        }
        if (espdResponseForm != null) {
            mainColumn.removeComponent(espdResponseForm);
            espdResponseForm = null;
        }
    }

    public Panel getDetailsPanel() {
        return detailsPanel;
    }

    public VerticalLayout getDetailsContent() {
        return detailsContent;
    }
}
