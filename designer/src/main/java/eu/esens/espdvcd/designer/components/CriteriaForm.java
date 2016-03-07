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

public class CriteriaForm extends VerticalLayout {
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private HorizontalLayout titleLayout = new HorizontalLayout();
    private FormLayout contentLayout = new FormLayout();
    private TextField ID = new TextField("Critera ID");
    private TextField typeCode = new TextField("Critera TypeCode");
    private TextField name = new TextField("Critera Name");
    private TextField description = new TextField("Critera Description");

    public CriteriaForm(SelectableCriterion criterion) {
        setMargin(true);
        this.addComponent(panel);
        //panelContent.addComponent(titleLayout);
        panelContent.addComponent(contentLayout);
        //titleLayout.addComponent(new Label("CRITERIA"));
        contentLayout.addComponent(ID);
        contentLayout.addComponent(typeCode);
        contentLayout.addComponent(name);
        contentLayout.addComponent(description);

        panel.setContent(panelContent);
        panel.setCaption("Criteria");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<SelectableCriterion> criteriaGroup = new BeanFieldGroup<>(SelectableCriterion.class);
        criteriaGroup.setItemDataSource(criterion);
        criteriaGroup.setBuffered(false);
        criteriaGroup.bindMemberFields(this);

        // Add a sub form
        for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(criterion, requirementGroup);
            contentLayout.addComponent(requirementGroupForm);
        }

/*        // Button that prints the values of the Bean/PROJ
        this.addComponent(new Button("New requirement group", (Button.ClickEvent event) -> {
            List<RequirementGroup> requirementGroups = criteria.getRequirementGroups();
            RequirementGroup requirementGroup = new RequirementGroup("", new ArrayList<Requirement>());
            requirementGroups.add(requirementGroup);

            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(criteria, requirementGroup);
            contentLayout.addComponent(requirementGroupForm);
        }));*/
    }
}
