package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.*;
import eu.esens.espdvcd.model.Criteria;
import eu.esens.espdvcd.model.Requirement;
import eu.esens.espdvcd.model.RequirementGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

import java.util.ArrayList;

/**
 * Created by ixuz on 2/24/16.
 */

public class CriteriaForm extends VerticalLayout {
    private HorizontalLayout titleLayout = new HorizontalLayout();
    private FormLayout contentLayout = new FormLayout();
    private TextField ID = new TextField("Critera ID");
    private TextField typeCode = new TextField("Critera TypeCode");
    private TextField name = new TextField("Critera Name");
    private TextField description = new TextField("Critera Description");

    public CriteriaForm(Criteria criteria) {
        this.addComponent(titleLayout);
        this.addComponent(contentLayout);
        titleLayout.addComponent(new Label("CRITERIA"));
        contentLayout.addComponent(ID);
        contentLayout.addComponent(typeCode);
        contentLayout.addComponent(name);
        contentLayout.addComponent(description);

        // Bind the this forms fields
        final BeanFieldGroup<Criteria> criteriaGroup = new BeanFieldGroup<>(Criteria.class);
        criteriaGroup.setItemDataSource(criteria);
        criteriaGroup.setBuffered(false);
        criteriaGroup.bindMemberFields(this);

        // Add a sub form
        for (RequirementGroup requirementGroup : criteria.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup);
            contentLayout.addComponent(requirementGroupForm);
        }

        // Button that prints the values of the Bean/PROJ
        titleLayout.addComponent(new Button("NEW", (Button.ClickEvent event) -> {
            ArrayList<RequirementGroup> requirementGroups = criteria.getRequirementGroups();
            RequirementGroup requirementGroup = new RequirementGroup("", new ArrayList<Requirement>());
            requirementGroups.add(requirementGroup);

            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup);
            contentLayout.addComponent(requirementGroupForm);
        }));
    }
}
