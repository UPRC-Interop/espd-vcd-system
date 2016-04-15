package eu.esens.espdvcd.designer.components;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ixuz on 4/8/16.
 */
public class ESPDResponseForm extends ESPDForm {

    private ESPDResponse espdResponse = null;
    private CheckBox selectAllExclusionCriteriaCheckbox = new CheckBox("Select all exclusion criteria");
    private CheckBox selectAllSelectionCriteriaCheckbox = new CheckBox("Select all selection criteria");
    private CheckBox selectAllEconomicOperatorCriteriaCheckbox = new CheckBox("Select all economic operator criteria");
    private CheckBox selectAllReductionCriteriaCheckbox = new CheckBox("Select all selection criteria");
    private HashMap<String,List<CriterionForm>> exclusionCriterionHash = new HashMap<>();
    private HashMap<String,List<CriterionForm>> selectionCriterionHash = new HashMap<>();
    private HashMap<String,List<CriterionForm>> economicOperatorCriterionHash = new HashMap<>();
    private HashMap<String,List<CriterionForm>> reductionCriterionHash = new HashMap<>();
    private List<CriterionGroupForm> exclusionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> selectionCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> economicOperatorCriterionGroupForms = new ArrayList<>();
    private List<CriterionGroupForm> reductionCriterionGroupForms = new ArrayList<>();

    public ESPDResponseForm(Master view, ESPDResponse espdResponse) {
        super(view, espdResponse, "espd.xml");
        this.espdResponse = espdResponse;

        // Page 1 - Procedure
        VerticalLayout page1 = newPage("Information concerning the procurement procedure", "Procedure");
        page1.addComponent(new CADetailsForm(espdResponse));
        page1.addComponent(new EODetailsForm(espdResponse.getEoDetails()));

        for (SelectableCriterion criterion : espdResponse.getEORelatedCriteriaList()) {

            if (!economicOperatorCriterionHash.containsKey(criterion.getTypeCode())) {
                economicOperatorCriterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true);
            economicOperatorCriterionHash.get(criterion.getTypeCode()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : economicOperatorCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = Codelists.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(fullTypeCodeName, criterionForms);
            economicOperatorCriterionGroupForms.add(criterionGroupForm);
            page1.addComponent(criterionGroupForm);
        }

        // Page 2 - Exclusion
        VerticalLayout page2 = newPage("Exclusion grounds criteria", "Exclusion");

        VerticalLayout exclusionActionLayout = new VerticalLayout();
        exclusionActionLayout.setMargin(true);
        exclusionActionLayout.addComponent(selectAllExclusionCriteriaCheckbox);
        selectAllExclusionCriteriaCheckbox.setValue(true);
        page2.addComponent(exclusionActionLayout);

        selectAllExclusionCriteriaCheckbox.addValueChangeListener(this::onSelectAllExclusionCriteria);
        selectAllExclusionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        for (SelectableCriterion criterion : espdResponse.getExclusionCriteriaList()) {

            if (!exclusionCriterionHash.containsKey(criterion.getTypeCode())) {
                exclusionCriterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true);
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
        VerticalLayout page3 = newPage("Selection criteria", "Selection");

        VerticalLayout selectionActionLayout = new VerticalLayout();
        selectionActionLayout.setMargin(true);
        selectionActionLayout.addComponent(selectAllSelectionCriteriaCheckbox);
        selectAllSelectionCriteriaCheckbox.setValue(true);
        page3.addComponent(selectionActionLayout);

        selectAllSelectionCriteriaCheckbox.addValueChangeListener(this::onSelectAllSelectionCriteria);
        selectAllSelectionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        for (SelectableCriterion criterion : espdResponse.getSelectionCriteriaList()) {

            if (!selectionCriterionHash.containsKey(criterion.getTypeCode())) {
                selectionCriterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true);
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
        VerticalLayout page4 = newPage("Reduction of candidates", "Reduction of candidates");

        VerticalLayout reductionActionLayout = new VerticalLayout();
        reductionActionLayout.setMargin(true);
        reductionActionLayout.addComponent(selectAllReductionCriteriaCheckbox);
        selectAllReductionCriteriaCheckbox.setValue(true);
        page4.addComponent(reductionActionLayout);

        selectAllReductionCriteriaCheckbox.addValueChangeListener(this::onSelectAllReductionCriteria);
        selectAllReductionCriteriaCheckbox.setStyleName("espdRequestForm-checkbox");

        for (SelectableCriterion criterion : espdResponse.getReductionOfCandidatesCriteriaList()) {

            if (!reductionCriterionHash.containsKey(criterion.getTypeCode())) {
                reductionCriterionHash.put(criterion.getTypeCode(), new ArrayList<CriterionForm>());
            }

            CriterionForm criterionForm = new CriterionForm(view, criterion, true);
            reductionCriterionHash.get(criterion.getTypeCode()).add(criterionForm);
        }

        for (Map.Entry<String, List<CriterionForm>> entry : reductionCriterionHash.entrySet()) {
            String key = entry.getKey();
            List<CriterionForm> criterionForms = entry.getValue();

            String fullTypeCodeName = Codelists.CriteriaType.getValueForId(key);

            if (fullTypeCodeName == null) {
                // Fallback to display the type code instead of real name
                fullTypeCodeName = key;
            }

            CriterionGroupForm criterionGroupForm = new CriterionGroupForm(fullTypeCodeName, criterionForms);
            reductionCriterionGroupForms.add(criterionGroupForm);
            page4.addComponent(new CriterionGroupForm(fullTypeCodeName, criterionForms));
        }

        // Page 5 - Finish
        VerticalLayout page5 = newPage("Finish", "Finish");
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



    /**
     * When the user have clicked the Export button, this method is invoked.
     * Exports the espd request xml to the system console
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    @Override
    protected void onExportConsole(Button.ClickEvent event) {
        // Display espd request xml button
        ESPDBuilder espdBuilder = new ESPDBuilder();
        System.out.println("espdResponse: " + espdResponse);
        String xml = espdBuilder.createXMLasString(espdResponse);
        System.out.println("Xml: " + xml);
    }
}
