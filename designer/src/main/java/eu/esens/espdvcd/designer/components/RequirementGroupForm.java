package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.Criteria;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import eu.esens.espdvcd.model.SelectableCriterion;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */

public class RequirementGroupForm extends VerticalLayout {
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private Label ID = new Label("RequirementGroupForm ID");

    public RequirementGroupForm(RequirementGroup requirementGroup) {
        setMargin(true);
        setStyleName("requirementGroupForm-layout");
        addComponent(panel);
        panel.setContent(panelContent);

        panelContent.addComponent(ID);

        panelContent.setMargin(true);
        panelContent.setStyleName("requirementGroupForm-panelContent");

        ID.setCaption("Requirement Group ID");
        ID.setValue(requirementGroup.getID());

        panel.setStyleName("requirementGroupForm-panel");
        panel.setCaption("Requirement group");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<RequirementGroup> requirementGroupGroup = new BeanFieldGroup<>(RequirementGroup.class);
        requirementGroupGroup.setItemDataSource(requirementGroup);
        requirementGroupGroup.setBuffered(false);
        requirementGroupGroup.bindMemberFields(this);

        // Add a sub form
        for (Requirement requirement : requirementGroup.getRequirements()) {
            RequirementForm requirementForm = new RequirementForm(requirement);
            panelContent.addComponent(requirementForm);
        }
    }
}
