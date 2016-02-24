package eu.esens.espdvcd.designer.components;

import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Created by ixuz on 2/24/16.
 */
public class RequirementForm extends FormLayout {
    private TextField ID = new TextField("Requirement ID");
    private TextField responseDataType = new TextField("Requirement DataType");
    private TextField description = new TextField("Requirement Description");

    public RequirementForm(Requirement requirement) {
        this.addComponent(ID);
        this.addComponent(responseDataType);
        this.addComponent(description);

        // Bind the this forms fields
        final BeanFieldGroup<Requirement> requirementGroup = new BeanFieldGroup<>(Requirement.class);
        requirementGroup.setItemDataSource(requirement);
        requirementGroup.setBuffered(false);
        requirementGroup.bindMemberFields(this);
    }
}
