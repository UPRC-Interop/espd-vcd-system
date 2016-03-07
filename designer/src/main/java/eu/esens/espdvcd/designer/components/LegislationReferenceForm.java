package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.RequirementGroup;

/**
 * Created by ixuz on 3/7/16.
 */
public class LegislationReferenceForm extends VerticalLayout {

    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private TextField title = new TextField("Title");
    private TextField description = new TextField("Description");
    private TextField jurisdictionLevelCode = new TextField("JurisdictionLevelCode");
    private TextField article = new TextField("Article");
    private TextField URI = new TextField("URI");

    public LegislationReferenceForm(LegislationReference legislationReference) {
        setMargin(true);
        setStyleName("legislationReferenceForm-layout");
        addComponent(panel);
        panel.setContent(panelContent);
        panelContent.addComponent(title);
        panelContent.addComponent(description);
        panelContent.addComponent(jurisdictionLevelCode);
        panelContent.addComponent(article);
        panelContent.addComponent(URI);

        panel.setStyleName("legislationReferenceForm-panel");
        panel.setContent(panelContent);
        panel.setCaption("Legislation reference");
        panel.setIcon(FontAwesome.CHEVRON_DOWN);

        panelContent.setMargin(true);

        // Bind the this forms fields
        final BeanFieldGroup<LegislationReference> legislationReferenceGroup = new BeanFieldGroup<>(LegislationReference.class);
        legislationReferenceGroup.setItemDataSource(legislationReference);
        legislationReferenceGroup.setBuffered(false);
        legislationReferenceGroup.bindMemberFields(this);
    }
}
