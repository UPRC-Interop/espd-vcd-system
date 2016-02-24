package eu.esens.espdvcd.designer.components;

import eu.esens.espdvcd.model.Criteria;
import eu.esens.espdvcd.model.RequirementGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Created by ixuz on 2/24/16.
 */

public class CriteriaForm extends FormLayout {
    private TextField ID = new TextField("Critera ID");
    private TextField typeCode = new TextField("Critera TypeCode");
    private TextField name = new TextField("Critera Name");
    private TextField description = new TextField("Critera Description");

    public CriteriaForm(Criteria criteria) {
        this.addComponent(ID);
        this.addComponent(typeCode);
        this.addComponent(name);
        this.addComponent(description);

        // Bind the this forms fields
        final BeanFieldGroup<Criteria> criteriaGroup = new BeanFieldGroup<>(Criteria.class);
        criteriaGroup.setItemDataSource(criteria);
        criteriaGroup.setBuffered(false);
        criteriaGroup.bindMemberFields(this);

        // Add a sub form
        for (RequirementGroup requirementGroup : criteria.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup);
            this.addComponent(requirementGroupForm);
        }
    }
}
