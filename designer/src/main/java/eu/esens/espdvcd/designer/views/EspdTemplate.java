package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ModelBuilder;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.model.ESPDRequest;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EspdTemplate extends Master {

    private HorizontalLayout panels = null;
    private ESPDRequest espdRequest = null;
    private ESPDRequestForm espdRequestForm = null;
    VerticalLayout panelLeftLayout = new VerticalLayout();
    VerticalLayout panelRightLayout = new VerticalLayout();
    Button panelLeftButtonNew = new Button("Create new ESPD Template");
    Button panelRightButtonImport = new Button("Import existing ESPD Artifact");
    private CriterionForm highlightedCriterion = null;
    private Panel uploadPanel = new Panel();
    GridLayout gridLayout = new GridLayout(2,1);
    VerticalLayout mainColumn = new VerticalLayout();

    protected Panel detailsPanel = new Panel();
    protected VerticalLayout detailsContent = new VerticalLayout();

    public EspdTemplate(Navigator navigator) {
        super(navigator, true);

        detailsPanel.setStyleName("detailsPanel");
        detailsPanel.setSizeFull();
        detailsPanel.setContent(detailsContent);

        detailsContent.setHeight("100%");
        detailsContent.setStyleName("master-detailsContent");
        detailsContent.setWidth("100%");

        mainContent.setWidth("100%");
        gridLayout.setWidth("100%");
        gridLayout.setHeight("100%");
        gridLayout.setColumnExpandRatio(0, 0.65f);
        gridLayout.setColumnExpandRatio(1, 0.35f);

        gridLayout.addComponent(mainColumn, 0, 0);
        gridLayout.addComponent(detailsPanel, 1, 0);
        mainContent.addComponent(gridLayout);

        panels = new HorizontalLayout();
        panels.setStyleName("EspdTemplate-panels");
        panels.setWidth("100%");
        mainColumn.addComponent(panels);

        Panel panelLeft = new Panel("Create new ESPD Template");
        panelLeft.setStyleName("EspdTemplate-panelLeft");
        panels.addComponent(panelLeft);

        panelLeftLayout.setStyleName("panelLeftLayout");
        panelLeft.setContent(panelLeftLayout);

        Label panelLeftDescription = new Label("You can create an ESPD Request from scratch. "
                + "The ESPD Request created will have an empty procurement details structure. "
                + "It will contain the default, predefined ESPD Criteria selection and exclusion criteria pre selected."
                + "You can deselect any of the criteria you do not require in the UI");
        panelLeftLayout.addComponent(panelLeftDescription);

        panelLeftButtonNew.setStyleName("espdTemplate-panelButton");
        panelLeftButtonNew.addClickListener(this::onNewEspdTemplate);
        panelLeftLayout.addComponent(panelLeftButtonNew);

        Panel panelRight = new Panel("Import existing ESPD Template 123123");
        panelRight.setStyleName("EspdTemplate-panelRight");
        panels.addComponent(panelRight);

        panelRightLayout.setStyleName("panelRightLayout");
        panelRight.setContent(panelRightLayout);

        Label panelRightDescription = new Label("You can create an ESPD Request by importing an existing ESPD XML Artifact. "
                + "The ESPD Request created will have the procurement details structure of the imported artifact. "
                + "It will contain the default, predefined ESPD Criteria selection and exclusion criteria. "
                + "The criteria existing in the imported document will be pre-selected."
                + "You can deselect any of the criteria you do not require in the UI");
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
        try {
            panels.setVisible(false);
            
            espdRequest = new ModelBuilder().addDefaultESPDCriteriaList().createESPDRequest();
            
            // Cenerate the espd request form base on the provided espd request model
            espdRequestForm = new ESPDRequestForm(this, espdRequest, -1, false);
            mainColumn.addComponent(espdRequestForm);
        } catch (BuilderException ex) {
            Logger.getLogger(EspdTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                    file = File.createTempFile("espdTemplate", ".xml");
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
                    
                    espdRequest = new ModelBuilder().importFrom(is).createESPDRequest();
                    espdRequestForm = new ESPDRequestForm(thisView, espdRequest, -1, false);
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
        gridLayout.removeComponent(espdRequestForm);
        panelRightLayout.removeComponent(uploadPanel);
        getDetailsContent().removeAllComponents();
        mainColumn.removeComponent(espdRequestForm);
    }

    public Panel getDetailsPanel() {
        return detailsPanel;
    }

    public VerticalLayout getDetailsContent() {
        return detailsContent;
    }
}
