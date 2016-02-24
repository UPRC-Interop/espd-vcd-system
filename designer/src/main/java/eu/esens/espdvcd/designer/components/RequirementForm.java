package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.*;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button.ClickEvent;

import java.util.ArrayList;

/**
 * Created by ixuz on 2/24/16.
 */
public class RequirementForm extends VerticalLayout {
    private HorizontalLayout titleLayout = new HorizontalLayout();
    private FormLayout contentLayout = new FormLayout();
    private TextField ID = new TextField("Requirement ID");
    private TextField responseDataType = new TextField("Requirement DataType");
    private TextField description = new TextField("Requirement Description");

    public RequirementForm(Requirement requirement) {
        this.addComponent(titleLayout);
        this.addComponent(contentLayout);
        titleLayout.addComponent(new Label("REQUIREMENT"));
        contentLayout.addComponent(ID);
        contentLayout.addComponent(responseDataType);
        contentLayout.addComponent(description);

        // Bind the this forms fields
        final BeanFieldGroup<Requirement> requirementGroup = new BeanFieldGroup<>(Requirement.class);
        requirementGroup.setItemDataSource(requirement);
        requirementGroup.setBuffered(false);
        requirementGroup.bindMemberFields(this);

        // Button that prints the values of the Bean/PROJ
        titleLayout.addComponent(new Button("DELETE", (Button.ClickEvent event) -> {
            // TODO: Delete item
        }));
    }
}
