package eu.esens.espdvcd.designer.components;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import org.hibernate.criterion.Criterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ixuz on 4/8/16.
 */
public class ESPDRequestForm extends ESPDForm {

    private CheckBox selectAllExclusionCriteriaCheckbox = new CheckBox("Select all exclusion criteria");
    private CheckBox selectAllSelectionCriteriaCheckbox = new CheckBox("Select all selection criteria");
    private CheckBox selectAllReductionCriteriaCheckbox = new CheckBox("Select all selection criteria");
    private List<CriterionGroupForm> exclusionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> selectionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> reductionCriterionGroupForms = new ArrayList<>();

    public ESPDRequestForm(Master view, ESPDRequest espdRequest) {
        super(view, espdRequest);


        // Page 1 - Procedure
        VerticalLayout page1 = newPage("Information concerning the procurement procedure", "Procedure");
        page1.addComponent(new CADetailsForm(espdRequest));


        // Page 2 - Exclusion
        VerticalLayout page2 = newPage("Exclusion grounds criteria", "Exclusion");

        VerticalLayout exclusionActionLayout = new VerticalLayout();
        exclusionActionLayout.setMargin(true);
        exclusionActionLayout.addComponent(selectAllExclusionCriteriaCheckbox);
        selectAllExclusionCriteriaCheckbox.setValue(true);
        page2.addComponent(exclusionActionLayout);

        selectAllExclusionCriteriaCheckbox.addValueChangeListener(this::onSelectAllExclusionCriteria);
        selectAllExclusionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        criterionGroupForm(view, espdRequest.getExclusionCriteriaList(),exclusionCriterionGroupForms,page2);

        // Page 3 - Selection
        VerticalLayout page3 = newPage("Selection criteria", "Selection");

        VerticalLayout selectionActionLayout = new VerticalLayout();
        selectionActionLayout.setMargin(true);
        selectionActionLayout.addComponent(selectAllSelectionCriteriaCheckbox);
        selectAllSelectionCriteriaCheckbox.setValue(true);
        page3.addComponent(selectionActionLayout);

        selectAllSelectionCriteriaCheckbox.addValueChangeListener(this::onSelectAllSelectionCriteria);
        selectAllSelectionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");
        criterionGroupForm(view, espdRequest.getSelectionCriteriaList(),selectionCriterionGroupForms,page3);

        // Page 4 - Reduction of candidates
        VerticalLayout page4 = newPage("Reduction of candidates", "Reduction of candidates");

        VerticalLayout reductionActionLayout = new VerticalLayout();
        reductionActionLayout.setMargin(true);
        reductionActionLayout.addComponent(selectAllReductionCriteriaCheckbox);
        selectAllReductionCriteriaCheckbox.setValue(true);
        page4.addComponent(reductionActionLayout);

        selectAllReductionCriteriaCheckbox.addValueChangeListener(this::onSelectAllReductionCriteria);
        selectAllReductionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");


       criterionGroupForm(view, espdRequest.getReductionOfCandidatesCriteriaList(),reductionCriterionGroupForms,page4);

        // Page 5 - Finish
        VerticalLayout page5 = newPage("Finish", "Finish");
    }



    public void criterionGroupForm(Master view, List<SelectableCriterion> criterionList, List<CriterionGroupForm> criterionGroupForms, VerticalLayout page)
    {
        HashMap<String,List<CriterionForm>> criterionHash = new HashMap<>();
        for (SelectableCriterion criterion : criterionList) {

            if (!criterionHash.containsKey(criterion.getTypeCode())) {
                criterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion);
            criterionHash.get(criterion.getTypeCode()).add(criterionForm);
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

