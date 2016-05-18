package eu.esens.espdvcd.designer.components;

 import com.vaadin.event.LayoutEvents;
 import com.vaadin.server.FontAwesome;
 import com.vaadin.ui.*;

 import java.util.List;

public class CriterionGroupForm extends VerticalLayout{
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private List<CriterionForm> criterionForms;

    public CriterionGroupForm(String caption, List<CriterionForm> criterionFormList) {
        this.criterionForms = criterionFormList;
        this.addLayoutClickListener(this::onCriterionGroupClick);
        this.addComponent(panel);
        for (CriterionForm criterionForm : criterionFormList) {
            panelContent.addComponent(criterionForm);
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
}
