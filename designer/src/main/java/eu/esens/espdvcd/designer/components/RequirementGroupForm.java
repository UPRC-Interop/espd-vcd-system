package eu.esens.espdvcd.designer.components;

import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroupForm extends FormLayout {
    private TextField ID = new TextField("RequirementGroupForm ID");

    public RequirementGroupForm(RequirementGroup requirementGroup) {
        this.addComponent(ID);

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);

        // Add a sub form
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirement);
            this.addComponent(requirementForm);
        }
    }
}
