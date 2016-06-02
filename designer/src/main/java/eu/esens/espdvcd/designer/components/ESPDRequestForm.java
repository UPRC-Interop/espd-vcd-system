package eu.esens.espdvcd.designer.components;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import java.util.*;

public class ESPDRequestForm extends ESPDForm {

    private CheckBox selectAllExclusionCriteriaCheckbox = new CheckBox("Select all exclusion criteria");
    private CheckBox selectAllSelectionCriteriaCheckbox = new CheckBox("Select all selection criteria");
    private CheckBox selectAllEconomicOperatorCriteriaCheckbox = new CheckBox("Select all economic operator criteria");
    private CheckBox selectAllReductionCriteriaCheckbox = new CheckBox("Select all reduction of candidates criteria");
    private List<CriterionGroupForm> exclusionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> selectionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> economicOperatorCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> reductionCriterionGroupForms = new ArrayList<>();

    public ESPDRequestForm(Master view, ESPDRequest espdRequest, boolean readOnly) {
        super(view, espdRequest, "espd_template.xml");

        // Page 1 - Procedure
        VerticalLayout page1 = newPage("Information concerning the procurement procedure", "Procedure");
        page1.addComponent(new CADetailsForm(espdRequest, readOnly));


        // Page 2 - Exclusion
        VerticalLayout page2 = newPage("Exclusion grounds criteria", "Exclusion");

        VerticalLayout exclusionActionLayout = new VerticalLayout();
        exclusionActionLayout.setMargin(true);
        exclusionActionLayout.addComponent(selectAllExclusionCriteriaCheckbox);
        selectAllExclusionCriteriaCheckbox.setValue(true);
        selectAllExclusionCriteriaCheckbox.setReadOnly(readOnly);
        page2.addComponent(exclusionActionLayout);

        selectAllExclusionCriteriaCheckbox.addValueChangeListener(this::onSelectAllExclusionCriteria);
        selectAllExclusionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(view, espdRequest.getExclusionCriteriaList(),exclusionCriterionGroupForms,page2, readOnly);

        // Page 3 - Selection
        VerticalLayout page3 = newPage("Selection criteria", "Selection");

        VerticalLayout selectionActionLayout = new VerticalLayout();
        selectionActionLayout.setMargin(true);
        selectionActionLayout.addComponent(selectAllSelectionCriteriaCheckbox);
        selectAllSelectionCriteriaCheckbox.setValue(true);
        selectAllSelectionCriteriaCheckbox.setReadOnly(readOnly);
        page3.addComponent(selectionActionLayout);

        selectAllSelectionCriteriaCheckbox.addValueChangeListener(this::onSelectAllSelectionCriteria);
        selectAllSelectionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(view, espdRequest.getSelectionCriteriaList(),selectionCriterionGroupForms,page3, readOnly);

        // Page 4 - Reduction of candidates
        VerticalLayout page4 = newPage("Reduction of candidates", "Candidates");

        VerticalLayout reductionActionLayout = new VerticalLayout();
        reductionActionLayout.setMargin(true);
        reductionActionLayout.addComponent(selectAllReductionCriteriaCheckbox);
        selectAllReductionCriteriaCheckbox.setValue(true);
        selectAllReductionCriteriaCheckbox.setReadOnly(readOnly);
        page4.addComponent(reductionActionLayout);

        selectAllReductionCriteriaCheckbox.addValueChangeListener(this::onSelectAllReductionCriteria);
        selectAllReductionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");


        criterionGroupForm(view, espdRequest.getReductionOfCandidatesCriteriaList(),reductionCriterionGroupForms,page4, readOnly);

        // Page 5 - Data on economic operator
        VerticalLayout page5 = newPage("Data on economic operator", "Economic operator");

        VerticalLayout economicOperatorActionLayout = new VerticalLayout();
        economicOperatorActionLayout.setMargin(true);
        economicOperatorActionLayout.addComponent(selectAllEconomicOperatorCriteriaCheckbox);
        selectAllEconomicOperatorCriteriaCheckbox.setValue(true);
        selectAllEconomicOperatorCriteriaCheckbox.setReadOnly(readOnly);
        page5.addComponent(economicOperatorActionLayout);

        selectAllEconomicOperatorCriteriaCheckbox.addValueChangeListener(this::onSelectAllEconomicOperatorCriteria);
        selectAllEconomicOperatorCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(view, espdRequest.getEORelatedCriteriaList(),economicOperatorCriterionGroupForms,page5, readOnly);

        // Page 6 - Finish
        VerticalLayout page6 = newPage("Finish", "Finish");
    }



    public void criterionGroupForm(Master view, List<SelectableCriterion> criterionList, List<CriterionGroupForm> criterionGroupForms, VerticalLayout page, boolean readOnly)
    {
        LinkedHashMap<String,List<CriterionForm>> criterionHash = new LinkedHashMap<>();
        for (SelectableCriterion criterion : criterionList) {

            if (!criterionHash.containsKey(criterion.getCriterionGroup())) {
                criterionHash.put(criterion.getCriterionGroup(), new ArrayList<>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, false, readOnly);
            criterionHash.get(criterion.getCriterionGroup()).add(criterionForm);
        }
        for (Map.Entry<String, List<CriterionForm>> entry : criterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = Codelists.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(fullTypeCodeName, criterionForms);
            criterionGroupForms.add(criterionGroupForm);
            page.addComponent(new CriterionGroupForm(fullTypeCodeName, criterionForms));
        }
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
     * Selects or deselects all economic operator criteria depending on
     * the boolean value of the selectAllEconomicOperatorCriteria checkbox
     *
     * @param event Vaadin7 value change event
     */
    public void onSelectAllEconomicOperatorCriteria(Property.ValueChangeEvent event) {
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
    public void onSelectAllReductionCriteria(Property.ValueChangeEvent event) {
        for (CriterionGroupForm criterionGroupForm : reductionCriterionGroupForms) {
            criterionGroupForm.setSelectedOnAllCriteria(selectAllReductionCriteriaCheckbox.getValue());
        }
    }
}

