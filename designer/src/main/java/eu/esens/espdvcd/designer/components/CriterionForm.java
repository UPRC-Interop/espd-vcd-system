package eu.esens.espdvcd.designer.components;

import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.Designer;
import eu.esens.espdvcd.designer.views.EspdTemplate;
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
    private Label description = new Label("Criterion Description");
    private boolean useRequirements = true;

    public CriterionForm(Master view, SelectableCriterion criterion, boolean useRequirements) {
        this.view = view;
        this.criterionReference = criterion;
        this.useRequirements = useRequirements;

        this.addComponent(panel);
        panel.setContent(panelContent);

        setMargin(true);
        setStyleName("criterionForm-layout");


        // If this criterion form will contain the requirements it will need to be displayed with a more complex layout
        if (useRequirements) {

            HorizontalLayout columns = new HorizontalLayout();
            VerticalLayout columnA = new VerticalLayout();
            VerticalLayout columnB = new VerticalLayout();

            panelContent.addComponent(columns);
            columns.addComponent(columnA);
            columns.addComponent(columnB);

            columnA.addComponent(description);

            columnA.setWidth(400, Unit.PIXELS);

            for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
                RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup, this.useRequirements);
                columnB.addComponent(requirementGroupForm);
            }
        } else { // The criterion form will contain a limited amount of details, therefore a more simplified layout is used.
            panelContent.addComponent(selected);
            panelContent.addComponent(description);
        }

        this.addLayoutClickListener(this::onCriterionClick);

        description.setValue(criterion.getDescription());

        panel.setCaption(criterion.getName());
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        panelContent.setMargin(true);
        panelContent.setStyleName("criterionForm-panelContent");


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
        } else if (!this.useRequirements) {
            view.getDetailsContent().removeAllComponents();
            Label detailsTitle = new Label(criterionReference.getName());
            detailsTitle.setStyleName("detailsTitle");
            view.getDetailsContent().addComponent(detailsTitle);
            if (criterionReference.getLegislationReference() != null) {
                view.getDetailsContent().addComponent(new LegislationReferenceForm(criterionReference.getLegislationReference()));
            }

            for (RequirementGroup requirementGroup : criterionReference.getRequirementGroups()) {
                view.getDetailsContent().addComponent(new RequirementGroupForm(requirementGroup, this.useRequirements));
            }

            if (view instanceof EspdTemplate) {
                EspdTemplate espdTemplateView = (EspdTemplate)view;
                CriterionForm highlightedCriterion = espdTemplateView.getHighlightedCriterion();
                if (highlightedCriterion != this) {
                    this.setHighlighted(true);
                    if (highlightedCriterion != null) {
                        highlightedCriterion.setHighlighted(false);
                    }
                    espdTemplateView.setHighlightedCriterion(this);
                }
            }
        }
    }

    void setHighlighted(boolean highlighted) {
        if (highlighted) {
            panel.addStyleName("criterionForm-panelContent-selected");
        } else {
            panel.removeStyleName("criterionForm-panelContent-selected");
        }
    }

    public void setSelected(boolean checkValue)
    {
        this.selected.setValue(checkValue);
    }
}
