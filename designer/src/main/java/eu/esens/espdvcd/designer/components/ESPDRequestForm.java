package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ixuz on 3/7/16.
 */
public class ESPDRequestForm extends VerticalLayout {

    private Master view;
    private ESPDRequest espdRequest = null;
    private HorizontalLayout progressBarLayout = new HorizontalLayout();
    private List<Label> progressBarLabels = new ArrayList<Label> ();
    private VerticalLayout page1 = new VerticalLayout();
    private VerticalLayout page2 = new VerticalLayout();
    private VerticalLayout page3 = new VerticalLayout();
    private List<VerticalLayout> pages = new ArrayList<>();
    private HorizontalLayout buttonList = new HorizontalLayout();
    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private Button exportConsole = new Button("Export to Console", FontAwesome.DOWNLOAD);
    private Button exportFile = new Button("Export to File", FontAwesome.DOWNLOAD);
    private int currentPageIndex = 0;

    public ESPDRequestForm(Master view, ESPDRequest espdRequest) {
        this.view = view;
        this.espdRequest = espdRequest;

        setWidth("100%");
        setStyleName("espdRequestForm-layout");

        addComponent(progressBarLayout);
        progressBarLayout.setStyleName("progressBarLayout");
        progressBarLayout.setWidth("100%");
        progressBarLayout.setSpacing(true);

        progressBarLabels.add(new Label("Procedure"));
        progressBarLabels.add(new Label("Exclusion"));
        progressBarLabels.add(new Label("Selection"));
        progressBarLabels.add(new Label("Finish"));

        for (Label progressBarLabel : progressBarLabels) {
            progressBarLayout.addComponent(progressBarLabel);
            progressBarLabel.setStyleName("progressBarLabel");
        }

        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        for (VerticalLayout page : pages) { addComponent(page); }
        addComponent(buttonList);
        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        buttonList.addComponent(exportConsole);
        buttonList.addComponent(exportFile);

        previous.addClickListener(this::onPrevious);
        cancel.addClickListener(this::onCancel);
        next.addClickListener(this::onNext);
        exportConsole.addClickListener(this::onExportConsole);

        previous.setStyleName("espdRequestForm-previous");
        cancel.setStyleName("espdRequestForm-cancel");
        next.setStyleName("espdRequestForm-next");
        exportConsole.setStyleName("espdRequestForm-finish");
        exportFile.setStyleName("espdRequestForm-finish");

        buttonList.setMargin(true);
        buttonList.setSpacing(true);

        page1.addComponent(new CADetailsForm(espdRequest));

        for (SelectableCriterion criterion : espdRequest.getExclusionCriteriaList()) {
            page2.addComponent(new CriterionForm(view, criterion));
        }
        for (SelectableCriterion criterion : espdRequest.getSelectionCriteriaList()) {
            page3.addComponent(new CriterionForm(view, criterion));
        }

        // Hook the exportFile button up with a downloadable resource
        StreamResource downloadableResource = new StreamResource(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                ESPDBuilder espdBuilder = new ESPDBuilder();
                String xml = espdBuilder.createXMLasString(espdRequest);
                byte[] xmlBytes = xml.getBytes();
                return new ByteArrayInputStream(xmlBytes);

            }
        }, "espd_template.xml");
        FileDownloader fileDownloader = new FileDownloader(downloadableResource);
        fileDownloader.extend(exportFile);

        showPage(currentPageIndex);
    }

    private void showPage(int pageIndex) {
        for (Label progressBarLabel : progressBarLabels) {
            progressBarLabel.removeStyleName("progressBarLabelHighlighted");
        }
        if (pageIndex >= 0 && pageIndex < progressBarLabels.size()) {
            progressBarLabels.get(pageIndex).addStyleName("progressBarLabelHighlighted");
        }

        for (int i=0; i<pages.size(); i++) {
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
        next.setEnabled((currentPageIndex+1 <= pages.size()-1));
        next.setVisible((currentPageIndex + 1 <= pages.size() - 1));
        previous.setEnabled((currentPageIndex-1 >= 0));
        previous.setVisible((currentPageIndex-1 >= 0));
        exportConsole.setEnabled(!(currentPageIndex + 1 <= pages.size() - 1));
        exportConsole.setVisible(!(currentPageIndex + 1 <= pages.size() - 1));
        exportFile.setEnabled(!(currentPageIndex + 1 <= pages.size() - 1));
        exportFile.setVisible(!(currentPageIndex + 1 <= pages.size() - 1));
    }

    /**
     * Displays the previous page of the form.
     * If there is no previous page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onPrevious(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex-1 >= 0 ? currentPageIndex-1 : currentPageIndex);
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
        System.out.println("Cancel process");
    }

    /**
     * Displays the next page of the form.
     * If there is no next page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onNext(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex+1 < pages.size() ? currentPageIndex+1 : currentPageIndex);
        showPage(currentPageIndex);
    }

    /**
     * When the user have clicked the Export button, this method is invoked.
     * Exports the espd request xml to the system console
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onExportConsole(Button.ClickEvent event) {
        // Display espd request xml button
        ESPDBuilder espdBuilder = new ESPDBuilder();
        String xml = espdBuilder.createXMLasString(espdRequest);
        System.out.println("Xml: " + xml);
    }
}
