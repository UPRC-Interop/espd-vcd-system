package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.*;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import eu.esens.espdvcd.model.RequirementGroup;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */
public class RequirementForm extends VerticalLayout {
    private RequirementGroup parentPojo = null;
    private Requirement requirementRef = null;
    private HorizontalLayout titleLayout = new HorizontalLayout();
    private FormLayout contentLayout = new FormLayout();
    private TextField ID = new TextField("Requirement ID");
    private TextField responseDataType = new TextField("Requirement DataType");
    private TextField description = new TextField("Requirement Description");

    public RequirementForm(RequirementGroup parentPojo, Requirement requirement) {
        this.parentPojo = parentPojo;
        this.requirementRef = requirement;
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

        // Button that deletes this element from both the parentPojo and the parents layout
        titleLayout.addComponent(new Button("Delete this requirement", (Button.ClickEvent event) -> {
            List<Requirement> requirements = parentPojo.getRequirements();
            for (Iterator<Requirement> iter = requirements.iterator(); iter.hasNext();) {
                Requirement item = iter.next();
                if (item == requirementRef) {
                    iter.remove();
                    AbstractLayout parentLayout = (AbstractLayout) this.getParent();
                    parentLayout.removeComponent(this);
                }
            }
        }));
    }
}
