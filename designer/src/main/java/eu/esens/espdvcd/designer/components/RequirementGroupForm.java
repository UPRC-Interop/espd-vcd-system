package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.Criteria;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroupForm extends VerticalLayout {
    private Criteria parentPojo = null;
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private RequirementGroup requirementGroupRef = null;
    private HorizontalLayout titleLayout = new HorizontalLayout();
    private FormLayout contentLayout = new FormLayout();
    private TextField ID = new TextField("RequirementGroupForm ID");

    public RequirementGroupForm(Criteria parentPojo, RequirementGroup requirementGroup) {
        this.addComponent(panel);
        this.parentPojo = parentPojo;
        this.requirementGroupRef = requirementGroup;
        //panelContent.addComponent(titleLayout);
        panelContent.addComponent(contentLayout);
        //titleLayout.addComponent(new Label("REQUIREMENT GROUP"));
        contentLayout.addComponent(ID);

        panel.setStyleName("requirementGroupForm-panel");
        panel.setContent(panelContent);
        panel.setCaption("Requirement group");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);

        // Add a sub form
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirementGroup, requirement);
            contentLayout.addComponent(requirementForm);
        }

/*        // Button that prints the values of the Bean/PROJ
        this.addComponent(new Button("New requirement", (Button.ClickEvent event) -> {
            List<Requirement> requirements = requirementGroup.getRequirements();
            Requirement requirement = new Requirement("", "", "");
            requirements.add(requirement);

            RequirementForm requirementForm = new RequirementForm(requirementGroup, requirement);
            contentLayout.addComponent(requirementForm);
        }));*/
/*
        // Button that deletes this element from both the parentPojo and the parents layout
        titleLayout.addComponent(new Button("Delete this requirement group", (Button.ClickEvent event) -> {
            List<RequirementGroup> requirements = parentPojo.getRequirementGroups();
            for (Iterator<RequirementGroup> iter = requirements.iterator(); iter.hasNext();) {
                RequirementGroup item = iter.next();
                if (item == requirementGroupRef) {
                    iter.remove();
                    AbstractLayout parentLayout = (AbstractLayout) this.getParent();
                    parentLayout.removeComponent(this);
                }
            }
        }));*/
    }
}
