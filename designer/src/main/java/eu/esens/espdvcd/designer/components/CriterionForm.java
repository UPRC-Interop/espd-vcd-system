package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.*;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */

public class CriterionForm extends VerticalLayout {
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private CheckBox selected = new CheckBox("Select this criterion?");
    private Label ID = new Label("Criterion ID");
    private Label typeCode = new Label("Criterion TypeCode");
    private Label name = new Label("Criterion Name");
    private Label description = new Label("Criterion Description");

    public CriterionForm(SelectableCriterion criterion) {
        setMargin(true);
        setStyleName("criterionForm-layout");
        this.addComponent(panel);
        panelContent.addComponent(selected);
        panelContent.addComponent(ID);
        panelContent.addComponent(typeCode);
        panelContent.addComponent(name);
        panelContent.addComponent(description);

        ID.setCaption("Criterion ID");
        ID.setValue(criterion.getID());

        typeCode.setCaption("Criterion Type Code");
        typeCode.setValue(criterion.getTypeCode());

        name.setCaption("Criterion Name");
        name.setValue(criterion.getName());

        description.setCaption("Criterion Description");
        description.setValue(criterion.getDescription());

        panelContent.setMargin(true);
        panelContent.setStyleName("criterionForm-panelContent");

        panel.setContent(panelContent);
        panel.setCaption("Criteria");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<SelectableCriterion> criteriaGroup = new BeanFieldGroup<>(SelectableCriterion.class);
        criteriaGroup.setItemDataSource(criterion);
        criteriaGroup.setBuffered(false);
        criteriaGroup.bindMemberFields(this);

        if (criterion.getLegislationReference() != null) {
            LegislationReferenceForm legislationReferenceForm = new LegislationReferenceForm(criterion.getLegislationReference());
            panelContent.addComponent(legislationReferenceForm);
        }
        // Add a sub form
        for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup);
            panelContent.addComponent(requirementGroupForm);
        }
    }
}
