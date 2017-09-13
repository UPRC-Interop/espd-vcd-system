package eu.esens.espdvcd.designer.components;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.designer.components.windows.CriterionGroupWindow;
import eu.esens.espdvcd.designer.components.windows.CriterionWindow;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import java.util.*;

public class ESPDRequestForm extends ESPDForm {

    ESPDRequest espd;
    Master view;
    VerticalLayout page1;
    VerticalLayout page2;
    VerticalLayout page3;
    VerticalLayout page4;
    VerticalLayout page5;
    VerticalLayout page6;
    VerticalLayout page7;

    private CheckBox selectAllExclusionCriteriaCheckbox = new CheckBox("Select all exclusion criteria");
    private CheckBox selectAllSelectionCriteriaCheckbox = new CheckBox("Select all selection criteria");
    private CheckBox selectAllEconomicOperatorCriteriaCheckbox = new CheckBox("Select all economic operator criteria");
    private CheckBox selectAllReductionCriteriaCheckbox = new CheckBox("Select all reduction of candidates criteria");
    private List<CriterionGroupForm> exclusionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> selectionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> economicOperatorCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> reductionCriterionGroupForms = new ArrayList<>();

    private CriterionGroupForm manuallyAddedCriteriaGroupForm;

    public ESPDRequestForm(Master view, ESPDRequest espdRequest, int displayEvidences, boolean readOnly) {
        super(view, espdRequest, "espd_template.xml");
        this.view = view;
        this.espd = espd;

        manuallyAddedCriteriaGroupForm = new CriterionGroupForm(espdRequest, view, "Manually added criteria", null);

        // Page 1 - Procedure
        page1 = newPage("Information concerning the procurement procedure", "Procedure");
        page1.addComponent(new CADetailsForm(espdRequest, readOnly));


        // Page 2 - Exclusion
        page2 = newPage("Exclusion grounds criteria", "Exclusion");

        VerticalLayout exclusionActionLayout = new VerticalLayout();
        exclusionActionLayout.setMargin(true);
        exclusionActionLayout.addComponent(selectAllExclusionCriteriaCheckbox);
        selectAllExclusionCriteriaCheckbox.setValue(true);
        selectAllExclusionCriteriaCheckbox.setReadOnly(readOnly);
        page2.addComponent(exclusionActionLayout);

        addNewCriterionButton(espdRequest, page2, displayEvidences);

        selectAllExclusionCriteriaCheckbox.addValueChangeListener(this::onSelectAllExclusionCriteria);
        selectAllExclusionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        VerticalLayout criterionGroups = new VerticalLayout();
        page2.addComponent(criterionGroups);
        criterionGroupForm(espdRequest, view, espdRequest.getExclusionCriteriaList(), exclusionCriterionGroupForms, criterionGroups, readOnly);

        addNewCriterionButton(espdRequest, page2, displayEvidences);

        // Page 3 - Selection
        page3 = newPage("Selection criteria", "Selection");

        VerticalLayout selectionActionLayout = new VerticalLayout();
        selectionActionLayout.setMargin(true);
        selectionActionLayout.addComponent(selectAllSelectionCriteriaCheckbox);
        selectAllSelectionCriteriaCheckbox.setValue(true);
        selectAllSelectionCriteriaCheckbox.setReadOnly(readOnly);
        page3.addComponent(selectionActionLayout);

        addNewCriterionButton(espdRequest, page3, displayEvidences);

        selectAllSelectionCriteriaCheckbox.addValueChangeListener(this::onSelectAllSelectionCriteria);
        selectAllSelectionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(espdRequest, view, espdRequest.getSelectionCriteriaList(),selectionCriterionGroupForms,page3, readOnly);

        addNewCriterionButton(espdRequest, page3, displayEvidences);

        // Page 4 - Reduction of candidates
        page4 = newPage("Reduction of candidates", "Candidates");

        VerticalLayout reductionActionLayout = new VerticalLayout();
        reductionActionLayout.setMargin(true);
        reductionActionLayout.addComponent(selectAllReductionCriteriaCheckbox);
        selectAllReductionCriteriaCheckbox.setValue(true);
        selectAllReductionCriteriaCheckbox.setReadOnly(readOnly);
        page4.addComponent(reductionActionLayout);

        addNewCriterionButton(espdRequest, page4, displayEvidences);

        selectAllReductionCriteriaCheckbox.addValueChangeListener(this::onSelectAllReductionCriteria);
        selectAllReductionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(espdRequest, view, espdRequest.getReductionOfCandidatesCriteriaList(),reductionCriterionGroupForms,page4, readOnly);

        addNewCriterionButton(espdRequest, page4, displayEvidences);

        // Page 5 - Data on economic operator
        page5 = newPage("Data on economic operator", "Economic operator");

        VerticalLayout economicOperatorActionLayout = new VerticalLayout();
        economicOperatorActionLayout.setMargin(true);
        economicOperatorActionLayout.addComponent(selectAllEconomicOperatorCriteriaCheckbox);
        selectAllEconomicOperatorCriteriaCheckbox.setValue(true);
        selectAllEconomicOperatorCriteriaCheckbox.setReadOnly(readOnly);
        page5.addComponent(economicOperatorActionLayout);

        addNewCriterionButton(espdRequest, page5, displayEvidences);

        selectAllEconomicOperatorCriteriaCheckbox.addValueChangeListener(this::onSelectAllEconomicOperatorCriteria);
        selectAllEconomicOperatorCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(espdRequest, view, espdRequest.getEORelatedCriteriaList(),economicOperatorCriterionGroupForms,page5, readOnly);

        addNewCriterionButton(espdRequest, page5, displayEvidences);

        // Page 6 - Manually added criteria
        page6 = newPage("Manually added criteria", "Extra criteria");
        addNewCriterionButton(espdRequest, page6, displayEvidences);
        page6.addComponent(manuallyAddedCriteriaGroupForm);
        addNewCriterionButton(espdRequest, page6, displayEvidences);

        // Page 7 - Finish
        page7 = newPage("Finish", "Finish");

        setExportXmlResource();
    }

    public void criterionGroupForm(ESPDRequest espd, Master view, List<SelectableCriterion> criterionList, List<CriterionGroupForm> criterionGroupForms, VerticalLayout page, boolean readOnly)
    {
        LinkedHashMap<String,List<CriterionForm>> criterionHash = new LinkedHashMap<>();
        for (SelectableCriterion criterion : criterionList) {

            if (!criterionHash.containsKey(criterion.getCriterionGroup())) {
                criterionHash.put(criterion.getCriterionGroup(), new ArrayList<>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, false, -1, readOnly, null);
            criterionHash.get(criterion.getCriterionGroup()).add(criterionForm);
        }
        for (Map.Entry<String, List<CriterionForm>> entry : criterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = Codelists.forV1.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(espd, view, fullTypeCodeName, criterionForms);
            criterionGroupForms.add(criterionGroupForm);
            page.addComponent(new CriterionGroupForm(espd, view, fullTypeCodeName, criterionForms));
        }
    }

    /**
     * Selects or deselects all exclusion criteria depending on
     * the boolean value of the selectAllExclusionCriteriaCheckbox checkbox
     *
     * @param event Vaadin7 value change event
     */
    public void onSelectAllExclusionCriteria(ValueChangeEvent event) {
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
    public void onSelectAllSelectionCriteria(ValueChangeEvent event) {
        for (CriterionGroupForm criterionGroupForm : selectionCriterionGroupForms) {
            criterionGroupForm.setSelectedOnAllCriteria(selectAllSelectionCriteriaCheckbox.getValue());
        }
    }

    /**
     * Selects or deselects all economic operator criteria depending on
     * the boolean value of the selectAllEconomicOperatorCriteria checkbox
     *
     * @param event Vaadin7 value change event
     */
    public void onSelectAllEconomicOperatorCriteria(ValueChangeEvent event) {
        for (CriterionGroupForm criterionGroupForm : economicOperatorCriterionGroupForms) {
            criterionGroupForm.setSelectedOnAllCriteria(selectAllEconomicOperatorCriteriaCheckbox.getValue());
        }
    }

    /**
     * Selects or deselects all reduction criteria depending on
     * the boolean value of the selectAllReductionCriteria checkbox
     *
     * @param event Vaadin7 value change event
     */
    public void onSelectAllReductionCriteria(ValueChangeEvent event) {
        for (CriterionGroupForm criterionGroupForm : reductionCriterionGroupForms) {
            criterionGroupForm.setSelectedOnAllCriteria(selectAllReductionCriteriaCheckbox.getValue());
        }
    }

    private void addCriterion(Button.ClickEvent clickEvent) {
        CriterionGroupWindow criterionGroupWindow = new CriterionGroupWindow(espd, view, page2);
        criterionGroupWindow.setCaption("Criterion group window");
        UI.getCurrent().addWindow(criterionGroupWindow);
    }

    private void addNewCriterionButton(ESPDRequest espdRequest, AbstractLayout layout, int displayEvidences) {
        Button newCriterionButton = new Button("Add criterion", FontAwesome.PLUS);
        newCriterionButton.setStyleName("espdRequestForm-formButton");
        layout.addComponent(newCriterionButton);

        newCriterionButton.addClickListener((clickEvent) -> {
            CriterionWindow criterionWindow = new CriterionWindow(espdRequest, view, manuallyAddedCriteriaGroupForm.getCriteriaLayout(), displayEvidences);
            criterionWindow.setCaption("Criterion window");
            UI.getCurrent().addWindow(criterionWindow);
        });
    }
}

