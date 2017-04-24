package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.VCDDocumentBuilder;
import eu.esens.espdvcd.builder.XMLDocumentBuilder;
import eu.esens.espdvcd.designer.views.Espd;
import eu.esens.espdvcd.designer.views.EspdTemplate;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.designer.views.VCD;
import eu.esens.espdvcd.designer.views.Viewer;
import eu.esens.espdvcd.model.ESPDRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eu.esens.espdvcd.model.ESPDResponse;
import no.difi.asic.SignatureHelper;

public class ESPDForm extends VerticalLayout {

    private Master view;
    private ESPDRequest espdRequest = null;
    private HorizontalLayout progressBarLayout = new HorizontalLayout();
    private List<Label> progressBarLabels = new ArrayList<Label>();
    private List<VerticalLayout> pages = new ArrayList<>();
    private HorizontalLayout buttonList = new HorizontalLayout();
    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private Button exportConsole = new Button("Export to Console", FontAwesome.DOWNLOAD);
    private Button exportFile = new Button("Export to File", FontAwesome.DOWNLOAD);
    private int currentPageIndex = 0;
    private VerticalLayout pagesLayout = new VerticalLayout();

    private String exportFileName;

    private static final String KEY_STORE_RESOURCE_NAME = "kontaktinfo-client-test.jks";

    public ESPDForm(Master view, ESPDRequest espdRequest, String exportFileName) {
        this.view = view;
        this.espdRequest = espdRequest;
        this.exportFileName = exportFileName;

        setWidth("100%");
        setStyleName("espdRequestForm-layout");

        // Progress bar
        addComponent(progressBarLayout);
        progressBarLayout.setStyleName("progressBarLayout");
        progressBarLayout.setWidth("100%");
        progressBarLayout.setSpacing(true);

        addComponent(pagesLayout);
        pagesLayout.setStyleName("pagesLayout");
        pagesLayout.setWidth(100, Unit.PERCENTAGE);

        addComponent(buttonList);

        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        buttonList.addComponent(exportConsole);
        buttonList.addComponent(exportFile);

        buttonList.setMargin(true);
        buttonList.setSpacing(true);

        previous.addClickListener(this::onPrevious);
        cancel.addClickListener(this::onCancel);
        next.addClickListener(this::onNext);
        exportConsole.addClickListener(this::onExportConsole);

        previous.setStyleName("espdRequestForm-previous");
        cancel.setStyleName("espdRequestForm-cancel");
        next.setStyleName("espdRequestForm-next");
        exportConsole.setStyleName("espdRequestForm-finish");
        exportFile.setStyleName("espdRequestForm-finish");

        showPage(currentPageIndex);
    }

    private void showPage(int pageIndex) {
        for (Label progressBarLabel : progressBarLabels) {
            progressBarLabel.removeStyleName("progressBarLabelHighlighted");
        }
        if (pageIndex >= 0 && pageIndex < progressBarLabels.size()) {
            progressBarLabels.get(pageIndex).addStyleName("progressBarLabelHighlighted");
        }

        for (int i = 0; i < pages.size(); i++) {
            VerticalLayout page = pages.get(i);
            if (i == pageIndex) {
                page.setVisible(true);

            } else {
                page.setVisible(false);
            }
        }
        updateButtonList();
        view.getMainPanel().setScrollTop(0);
    }

    private void updateButtonList() {
        next.setEnabled((currentPageIndex + 1 <= pages.size() - 1));
        next.setVisible((currentPageIndex + 1 <= pages.size() - 1));
        previous.setEnabled((currentPageIndex - 1 >= 0));
        previous.setVisible((currentPageIndex - 1 >= 0));
        exportConsole.setEnabled(!(currentPageIndex + 1 <= pages.size() - 1));
        exportConsole.setVisible(!(currentPageIndex + 1 <= pages.size() - 1));
        exportFile.setEnabled(!(currentPageIndex + 1 <= pages.size() - 1));
        exportFile.setVisible(!(currentPageIndex + 1 <= pages.size() - 1));
    }

    public VerticalLayout newPage(String title, String TitleInProgressBar) {
        VerticalLayout pageLayout = new VerticalLayout();
        pageLayout.setSpacing(true);
        pageLayout.setStyleName("pageLayout");

        Label pageCaptionLabel = new Label(title);
        pageCaptionLabel.setStyleName("pageCaptionLabel");
        pageCaptionLabel.setSizeUndefined();
        pageLayout.addComponent(pageCaptionLabel);

        Label pageTitleInProgressBarLabel = new Label(TitleInProgressBar);
        pageTitleInProgressBarLabel.setStyleName("progressBarLabel");

        progressBarLabels.add(pageTitleInProgressBarLabel);
        progressBarLayout.addComponent(pageTitleInProgressBarLabel);
        pages.add(pageLayout);
        pagesLayout.addComponent(pageLayout);

        showPage(currentPageIndex);

        return pageLayout;
    }

    /**
     * Displays the previous page of the form. If there is no previous page,
     * this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onPrevious(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex - 1 >= 0 ? currentPageIndex - 1 : currentPageIndex);
        showPage(currentPageIndex);
    }

    /**
     * When the user have clicked the Cancel button, this method is invoked.
     * TODO: Implement logic for restarting/leaving the form page.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onCancel(Button.ClickEvent event) {
        if (view instanceof EspdTemplate) {
            EspdTemplate espdTemplateView = (EspdTemplate) view;
            espdTemplateView.resetView();
        }
        if (view instanceof Espd) {
            Espd espdView = (Espd) view;
            espdView.resetView();
        }
        if (view instanceof Viewer) {
            Viewer espdView = (Viewer) view;
            espdView.resetView();
        } 
        if (view instanceof VCD) {
            VCD vcdView = (VCD) view;
            vcdView.resetView();
        }
        
    }

    /**
     * Displays the next page of the form. If there is no next page, this method
     * will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onNext(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex + 1 < pages.size() ? currentPageIndex + 1 : currentPageIndex);
        showPage(currentPageIndex);
    }

    /**
     * When the user have clicked the Export button, this method is invoked.
     * Exports the espd request xml to the system console
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    protected void onExportConsole(Button.ClickEvent event) {
        // Display espd request xml button
        String xml = new XMLDocumentBuilder(espdRequest).getAsString();
        System.out.println("Xml: " + xml);
    }

    protected void setExportXmlResource() {
        StreamResource downloadableResource = new StreamResource(() -> {
            return (new XMLDocumentBuilder(espdRequest).getAsInputStream());
        }, exportFileName);

        FileDownloader fileDownloader = new FileDownloader(downloadableResource);
        fileDownloader.extend(exportFile);
    }

    protected void setExportAsicResource() {
        StreamResource downloadableResource = new StreamResource(() -> {
            InputStream asic = null;
            try {
                SignatureHelper signature = new SignatureHelper(getKeyStoreFile(), keyStorePassword(), privateKeyPassword());
                VCDDocumentBuilder db = new VCDDocumentBuilder(espdRequest, signature);
                asic = db.getAsInputStream();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return (asic);
        }, "VCD.asice");

        FileDownloader fileDownloader = new FileDownloader(downloadableResource);
        fileDownloader.extend(exportFile);
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return key store file
     */
    private File getKeyStoreFile() throws IllegalStateException {

        URL keyStoreResourceURL = this.getClass().getClassLoader().getResource(KEY_STORE_RESOURCE_NAME);
        try {
            URI uri = keyStoreResourceURL.toURI();

            File file = new File(uri);
            if (!file.canRead()) {
                throw new IllegalStateException("Unable to locate " + KEY_STORE_RESOURCE_NAME + " in class path");
            }
            return file;

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to convert URL of keystore " + KEY_STORE_RESOURCE_NAME + " into a URI");
        }
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyStorePassword() {
        return "changeit";
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String privateKeyPassword() {
        return "changeit";
    }

    /**
     * temporary method for signing the ASiC - copied from no.difi.asic.TestUtil
     * @return
     */
    private String keyPairAlias() {
        return "client_alias";
    }
}
