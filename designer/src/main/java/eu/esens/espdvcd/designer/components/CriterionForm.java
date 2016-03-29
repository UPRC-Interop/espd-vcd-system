package eu.esens.espdvcd.designer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.Designer;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.designer.views.Start;
import eu.esens.espdvcd.model.*;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.LayoutEvents.LayoutClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ixuz on 2/24/16.
 */

public class CriterionForm extends VerticalLayout {
    private Master view;
    private Criterion criterionReference = null;
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private CheckBox selected = new CheckBox("Select this criterion?");
    //private Label ID = new Label("Criterion ID");
    //private Label typeCode = new Label("Criterion TypeCode");
    //private Label name = new Label("Criterion Name");

    private Label description = new Label("Criterion Description");

    public CriterionForm(Master view, SelectableCriterion criterion) {
        this.view = view;
        this.criterionReference = criterion;
        setMargin(true);
        setStyleName("criterionForm-layout");
        this.addComponent(panel);
        panelContent.addComponent(selected);
        //panelContent.addComponent(ID);
        //panelContent.addComponent(typeCode);
        //panelContent.addComponent(name);
        panelContent.addComponent(description);

        this.addLayoutClickListener(this::onCriterionClick);

        //ID.setCaption("Criterion ID");
        //ID.setValue(criterion.getID());

        //typeCode.setCaption("Criterion Type Code");
        //typeCode.setValue(criterion.getTypeCode());

        //name.setCaption("Criterion Name");
        //name.setValue(criterion.getName());

        //description.setCaption("Criterion Description");
        description.setValue(criterion.getDescription());

        panelContent.setMargin(true);
        panelContent.setStyleName("criterionForm-panelContent");

        panel.setContent(panelContent);
        panel.setCaption(criterion.getName());
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<SelectableCriterion> criteriaGroup = new BeanFieldGroup<>(SelectableCriterion.class);
        criteriaGroup.setItemDataSource(criterion);
        criteriaGroup.setBuffered(false);
        criteriaGroup.bindMemberFields(this);
    }

    void onCriterionClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof Panel && event.getClickedComponent() == panel) {
            if (!event.isDoubleClick()) {
                panelContent.setVisible(!panelContent.isVisible());

                if (panelContent.isVisible()) {
                    panel.setIcon(FontAwesome.CHEVRON_DOWN);
                } else {
                    panel.setIcon(FontAwesome.CHEVRON_RIGHT);
                }
            }
        } else {
            view.getDetailsContent().removeAllComponents();
            Label detailsTitle = new Label(criterionReference.getName());
            detailsTitle.setStyleName("detailsTitle");
            view.getDetailsContent().addComponent(detailsTitle);
            if (criterionReference.getLegislationReference() != null) {
                view.getDetailsContent().addComponent(new LegislationReferenceForm(criterionReference.getLegislationReference()));
            }

            for (RequirementGroup requirementGroup : criterionReference.getRequirementGroups()) {
                view.getDetailsContent().addComponent(new RequirementGroupForm(requirementGroup));
            }
        }
    }
}
