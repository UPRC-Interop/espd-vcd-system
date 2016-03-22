package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.Requirement;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

/**
 * Created by ixuz on 2/24/16.
 */
public class RequirementForm extends VerticalLayout {
    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    //private Label ID = new Label("Requirement ID");
    private Label responseDataType = new Label("Requirement DataType");
    private Label description = new Label("Requirement Description");

    public RequirementForm(Requirement requirement) {
        setMargin(true);
        setStyleName("requirementForm-layout");
        this.addComponent(panel);
        //panelContent.addComponent(ID);
        panelContent.addComponent(responseDataType);
        panelContent.addComponent(description);

        panelContent.setMargin(true);

        //ID.setCaption("Requirement ID");
        //ID.setValue(requirement.getID());

        responseDataType.setCaption("Requirement Response Data Type");
        responseDataType.setValue(requirement.getResponseDataType());

        description.setCaption("Requirement Description");
        description.setValue(requirement.getDescription());

        panel.setStyleName("requirementForm-panel");
        panel.setContent(panelContent);
        panel.setCaption("Requirement");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        // Bind the this forms fields
        final BeanFieldGroup<Requirement> requirementGroup = new BeanFieldGroup<>(Requirement.class);
        requirementGroup.setItemDataSource(requirement);
        requirementGroup.setBuffered(false);
        requirementGroup.bindMemberFields(this);
    }
}
