package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.components.ESPDResponseForm;
import eu.esens.espdvcd.model.*;

import java.io.*;

/**
 * Created by ixuz on 4/8/16.
 */
public class Espd extends Master {
    private HorizontalLayout panels = null;
    private ESPDResponse espdResponse = null;
    private ESPDResponseForm espdResponseForm = null;
    VerticalLayout panelRightLayout = new VerticalLayout();
    Button panelRightButtonImport = new Button("Import existing ESPD");
    private CriterionForm highlightedCriterion = null;
    private Panel uploadPanel = new Panel();

    public Espd(Navigator navigator) {
        super(navigator, true);

        panels = new HorizontalLayout();
        panels.setStyleName("EspdTemplate-panels");
        panels.setWidth("100%");
        mainContent.addComponent(panels);

        Panel panelRight = new Panel("Import existing ESPD");
        panels.addComponent(panelRight);

        panelRightLayout.setStyleName("panelRightLayout");
        panelRight.setContent(panelRightLayout);

        Label panelRightDescription = new Label("A description about importing an existing espd. More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here... More details goes here...");
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
                    espdResponse = espdBuilder.createESPDResponseFromXML(is);

                    if (espdResponse.getEoDetails() == null) { // <- I must do this to upon import
                        espdResponse.setEODetails(new EODetails()); // <- I must do this to upon import
                    }

                    espdResponseForm = new ESPDResponseForm(thisView, espdResponse);
                    mainContent.addComponent(espdResponseForm);
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
        getDetailsContent().removeAllComponents();
    }
}
