package eu.esens.espdvcd.designer.components;

 import com.vaadin.event.LayoutEvents;
 import com.vaadin.server.FontAwesome;
 import com.vaadin.ui.*;
 import eu.esens.espdvcd.designer.components.windows.CriterionGroupWindow;
 import eu.esens.espdvcd.designer.components.windows.CriterionWindow;
 import eu.esens.espdvcd.designer.views.Master;
 import eu.esens.espdvcd.model.ESPDRequest;

 import java.util.List;

public class CriterionGroupForm extends VerticalLayout {
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private List<CriterionForm> criterionForms;
    private VerticalLayout criteriaLayout;

    public CriterionGroupForm(ESPDRequest espd, Master view, String caption, List<CriterionForm> criterionFormList) {
        this.criterionForms = criterionFormList;
        this.addLayoutClickListener(this::onCriterionGroupClick);
        this.addComponent(panel);

        criteriaLayout = new VerticalLayout();
        panelContent.addComponent(criteriaLayout);
        if (criterionFormList != null) {
            for (CriterionForm criterionForm : criterionFormList) {
                criteriaLayout.addComponent(criterionForm);
            }
        }
        panel.setContent(panelContent);
        panel.setCaption(caption);
        panel.setIcon(FontAwesome.CHEVRON_DOWN);
        setWidth(100, Unit.PERCENTAGE);
        setStyleName("CriterionGroupForm");
        panel.setWidth(100, Unit.PERCENTAGE);
        panel.setStyleName("CriterionGroupFormPanel");
        panelContent.setStyleName("CriterionGroupFormPanelContent");
        panelContent.setWidth(100, Unit.PERCENTAGE);
/*
        Button newCriterionButton = new Button("Add criterion");
        panelContent.addComponent(newCriterionButton);

        newCriterionButton.addClickListener((clickEvent) -> {
            CriterionWindow criterionWindow = new CriterionWindow(espd, view, criteriaLayout);
            criterionWindow.setCaption("Criterion window");
            UI.getCurrent().addWindow(criterionWindow);
        });*/
    }

    public void setSelectedOnAllCriteria(boolean selected) {
        for (CriterionForm criterionForm : criterionForms) {
            criterionForm.setSelected(selected);
        }
    }

    void onCriterionGroupClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof Panel && event.getClickedComponent() == panel) {
            if (!event.isDoubleClick()) {
                panelContent.setVisible(!panelContent.isVisible());
            }
        }
    }

    public VerticalLayout getCriteriaLayout() {
        return criteriaLayout;
    }
}
