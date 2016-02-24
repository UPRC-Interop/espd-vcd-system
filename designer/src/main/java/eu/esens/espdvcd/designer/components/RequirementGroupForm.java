package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.*;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

import java.util.ArrayList;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroupForm extends VerticalLayout {
    private HorizontalLayout titleLayout = new HorizontalLayout();
    private FormLayout contentLayout = new FormLayout();
    private TextField ID = new TextField("RequirementGroupForm ID");

    public RequirementGroupForm(RequirementGroup requirementGroup) {
        this.addComponent(titleLayout);
        this.addComponent(contentLayout);
        titleLayout.addComponent(new Label("REQUIREMENT GROUP"));
        contentLayout.addComponent(ID);

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);

        // Add a sub form
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirement);
            contentLayout.addComponent(requirementForm);
        }

        // Button that prints the values of the Bean/PROJ
        titleLayout.addComponent(new Button("NEW", (Button.ClickEvent event) -> {
            ArrayList<Requirement> requirements = requirementGroup.getRequirements();
            Requirement requirement = new Requirement("", "", "");
            requirements.add(requirement);

            RequirementForm requirementForm = new RequirementForm(requirement);
            contentLayout.addComponent(requirementForm);
        }));
    }
}
