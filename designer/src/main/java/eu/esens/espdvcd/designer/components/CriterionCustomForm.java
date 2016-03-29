package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.SelectableCriterion;

/**
 * Created by Dell Latitude E6320 on 3/22/2016.
 */
public class CriterionCustomForm extends VerticalLayout{
    private Criterion criterionReference = null;
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private Label name = new Label("Criterion Name");

    //private Label description = new Label("Criterion Description");

    public CriterionCustomForm(Criterion criterion) {

        setMargin(true);
        setStyleName("criterionForm-layout");
        this.addComponent(panel);
        name.setCaption("Criterion Name");
        name.setValue(criterion.getName());
        panelContent.addComponent(name);
        panelContent.setMargin(true);
        panelContent.setStyleName("criterionForm-panelContent");

        panel.setContent(panelContent);
        panel.setCaption("Criterion");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);
    }
}
