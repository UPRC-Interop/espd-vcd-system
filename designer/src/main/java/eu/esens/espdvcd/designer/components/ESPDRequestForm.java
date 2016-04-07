package eu.esens.espdvcd.designer.components;

import com.vaadin.data.Property;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private VerticalLayout page4 = new VerticalLayout();
    private VerticalLayout page5 = new VerticalLayout();
    private List<VerticalLayout> pages = new ArrayList<>();
    private HorizontalLayout buttonList = new HorizontalLayout();
    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private Button exportConsole = new Button("Export to Console", FontAwesome.DOWNLOAD);
    private Button exportFile = new Button("Export to File", FontAwesome.DOWNLOAD);
    private CheckBox selectAllExclusionCriteriaCheckbox = new CheckBox("Select all criterion");
    private CheckBox selectAllSelectionCriteriaCheckbox = new CheckBox("Select all criterion");
    private int currentPageIndex = 0;
    private HashMap<String,List<CriterionForm>> exclusionCriterionHash = new HashMap<String,List<CriterionForm>>();
    private HashMap<String,List<CriterionForm>> selectionCriterionHash = new HashMap<String,List<CriterionForm>>();
    private List<CriterionGroupForm> exclusionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> selectionCriterionGroupForms = new ArrayList<>();

    public ESPDRequestForm(Master view, ESPDRequest espdRequest) {
        this.view = view;
        this.espdRequest = espdRequest;

        setWidth("100%");
        setStyleName("espdRequestForm-layout");


        // Progress bar
        addComponent(progressBarLayout);
        progressBarLayout.setStyleName("progressBarLayout");
        progressBarLayout.setWidth("100%");
        progressBarLayout.setSpacing(true);

        progressBarLabels.add(new Label("Procedure"));
        progressBarLabels.add(new Label("Exclusion"));
        progressBarLabels.add(new Label("Selection"));
        progressBarLabels.add(new Label("Reduction of candidates"));
        progressBarLabels.add(new Label("Finish"));

        for (Label progressBarLabel : progressBarLabels) {
            progressBarLayout.addComponent(progressBarLabel);
            progressBarLabel.setStyleName("progressBarLabel");
        }

        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        pages.add(page4);
        pages.add(page5);
        for (VerticalLayout page : pages) { addComponent(page); }
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


        // Page 1 - Procedure
        page1.setSpacing(true);
        page1.addComponent(new CADetailsForm(espdRequest));


        // Page 2 - Exclusion
        page2.setSpacing(true);

        VerticalLayout exclusionActionLayout = new VerticalLayout();
        exclusionActionLayout.setMargin(true);
        exclusionActionLayout.addComponent(selectAllExclusionCriteriaCheckbox);
        selectAllExclusionCriteriaCheckbox.setValue(true);
        page2.addComponent(exclusionActionLayout);

        selectAllExclusionCriteriaCheckbox.addValueChangeListener(this::onSelectAllExclusionCriteria);
        selectAllExclusionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        for (SelectableCriterion criterion : espdRequest.getExclusionCriteriaList()) {

            if (!exclusionCriterionHash.containsKey(criterion.getTypeCode())) {
                exclusionCriterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion);
            exclusionCriterionHash.get(criterion.getTypeCode()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : exclusionCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = Codelists.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(fullTypeCodeName, criterionForms);
            exclusionCriterionGroupForms.add(criterionGroupForm);
            page2.addComponent(criterionGroupForm);
        }


        // Page 3 - Selection
        page3.setSpacing(true);

        VerticalLayout selectionActionLayout = new VerticalLayout();
        selectionActionLayout.setMargin(true);
        selectionActionLayout.addComponent(selectAllSelectionCriteriaCheckbox);
        selectAllSelectionCriteriaCheckbox.setValue(true);
        page3.addComponent(selectionActionLayout);

        selectAllSelectionCriteriaCheckbox.addValueChangeListener(this::onSelectAllSelectionCriteria);
        selectAllSelectionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        for (SelectableCriterion criterion : espdRequest.getSelectionCriteriaList()) {

            if (!selectionCriterionHash.containsKey(criterion.getTypeCode())) {
                selectionCriterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion);
            selectionCriterionHash.get(criterion.getTypeCode()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : selectionCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = Codelists.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(fullTypeCodeName, criterionForms);
            selectionCriterionGroupForms.add(criterionGroupForm);
            page3.addComponent(new CriterionGroupForm(fullTypeCodeName, criterionForms));
        }


        // Page 4 - Reduction of candidates


        // Page 5 - Finish


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
     * Selects or deselects all exclusion criteria depending on
     * the boolean value of the selectAllExclusionCriteriaCheckbox checkbox
     *
     * @param event Vaadin7 value change event
     */
    public void onSelectAllExclusionCriteria(Property.ValueChangeEvent event) {
        for (CriterionGroupForm criterionGroupForm : exclusionCriterionGroupForms) {
            criterionGroupForm.setSelectedOnAllCriteria(selectAllExclusionCriteriaCheckbox.getValue());
        }
    }

    /**
     * Selects or deselects all selection criteria depending on
     * the boolean value of the selectAllSelectionCriteria checkbox
     *
     * @param event Vaadin7 value change event
     */
    public void onSelectAllSelectionCriteria(Property.ValueChangeEvent event) {
        for (CriterionGroupForm criterionGroupForm : selectionCriterionGroupForms) {
            criterionGroupForm.setSelectedOnAllCriteria(selectAllSelectionCriteriaCheckbox.getValue());
        }
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
