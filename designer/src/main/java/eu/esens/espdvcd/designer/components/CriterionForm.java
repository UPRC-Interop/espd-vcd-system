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
    private TextField ID = new TextField("Critera ID");
    private TextField typeCode = new TextField("Critera TypeCode");
    private TextField name = new TextField("Critera Name");
    private TextField description = new TextField("Critera Description");

    public CriterionForm(SelectableCriterion criterion) {
        setMargin(true);
        setStyleName("criterionForm-layout");
        this.addComponent(panel);
        panelContent.addComponent(ID);
        panelContent.addComponent(typeCode);
        panelContent.addComponent(name);
        panelContent.addComponent(description);

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

        LegislationReferenceForm legislationReferenceForm = new LegislationReferenceForm(criterion.getLegislationReference());
        panelContent.addComponent(legislationReferenceForm);

        // Add a sub form
        for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup);
            panelContent.addComponent(requirementGroupForm);
        }
    }
}
