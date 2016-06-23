package eu.esens.espdvcd.designer.components;

import eu.esens.espdvcd.designer.DetailsPanel.DetailsPanel;
import eu.esens.espdvcd.designer.DetailsPanel.DetailsPanelBuilder;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.views.EspdTemplate;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.*;
import com.vaadin.data.fieldgroup.BeanFieldGroup;


public class CriterionForm extends VerticalLayout {
    private Master view;
    private Criterion criterionReference = null;
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private CheckBox selected = new CheckBox("Select this criterion?");

    private Label description = new Label("Criterion Description");
    private boolean useRequirements = true;

    private boolean readOnly = false;

    public CriterionForm(Master view, SelectableCriterion criterion, boolean useRequirements, boolean readOnly) {
        this.view = view;
        this.criterionReference = criterion;
        this.useRequirements = useRequirements;
        this.readOnly = readOnly;

        this.addComponent(panel);
        panel.setContent(panelContent);

        setMargin(true);
        setStyleName("criterionForm-layout");

        panel.setCaption(criterion.getName());

        // If this criterion form will contain the requirements it will need to be displayed with a more complex layout
        if (useRequirements) {

            HorizontalLayout columns = new HorizontalLayout();
            VerticalLayout columnA = new VerticalLayout();
            VerticalLayout columnB = new VerticalLayout();

            panelContent.addComponent(columns);
            columns.addComponent(columnA);
            columns.addComponent(columnB);

            columnA.addComponent(description);

            columnA.setWidth(100, Unit.PERCENTAGE);
            columnB.setMargin(false);
            columnB.setSpacing(false);
            columnB.addStyleName("ignoreCaptionCellWidth");

            for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
                RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroup, this.useRequirements, readOnly);
                columnB.addComponent(requirementGroupForm);
            }

            if (criterionReference.getLegislationReference() != null) {
               columnA.addComponent(new LegislationReferenceForm(criterionReference.getLegislationReference()));
            }
        } else { // The criterion form will contain a limited amount of details, therefore a more simplified layout is used.
            panelContent.addComponent(selected);
            panelContent.addComponent(description);
        }

        this.addLayoutClickListener(this::onCriterionClick);

        description.setValue(criterion.getDescription());

        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        panelContent.setMargin(true);
        panelContent.setStyleName("criterionForm-panelContent");

        // Bind the this forms fields
        final BeanFieldGroup<SelectableCriterion> criteriaGroup = new BeanFieldGroup<>(SelectableCriterion.class);
        criteriaGroup.setItemDataSource(criterion);
        criteriaGroup.setBuffered(false);
        criteriaGroup.bindMemberFields(this);
        criteriaGroup.setReadOnly(readOnly);

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
            if (view instanceof EspdTemplate) {
                EspdTemplate espdTemplateView = (EspdTemplate) view;
                espdTemplateView.getDetailsContent().removeAllComponents();

                DetailsPanelBuilder detailsPanelBuilder = new DetailsPanelBuilder();

                detailsPanelBuilder.setCaption(criterionReference.getName());

                if (criterionReference.getLegislationReference() != null) {
                    detailsPanelBuilder.setLegislationReference(criterionReference.getLegislationReference());
                }

                detailsPanelBuilder.setRequirementGroups(criterionReference.getRequirementGroups());

                DetailsPanel detailsPanel = detailsPanelBuilder.build();
                espdTemplateView.getDetailsContent().addComponent(detailsPanel);

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
