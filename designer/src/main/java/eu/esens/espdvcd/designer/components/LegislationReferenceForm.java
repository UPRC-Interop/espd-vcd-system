package eu.esens.espdvcd.designer.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.model.LegislationReference;

/**
 * Created by ixuz on 3/7/16.
 */
public class LegislationReferenceForm extends VerticalLayout {

    private Panel panel = new Panel();
    private VerticalLayout panelContent = new VerticalLayout();
    private Label title = new Label("Legislation Reference Title");
    private Label description = new Label("Legislation Reference Description");
    private Label jurisdictionLevelCode = new Label("Legislation Reference JurisdictionLevelCode");
    private Label article = new Label("Legislation Reference Article");
    private Label URI = new Label("Legislation Reference URI");

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

        title.setCaption("Legislation Reference Title");
        title.setValue(legislationReference.getTitle());

        description.setCaption("Legislation Reference Description");
        description.setValue(legislationReference.getDescription());

        jurisdictionLevelCode.setCaption("Legislation Reference Jurisdiction Level Code");
        jurisdictionLevelCode.setValue(legislationReference.getJurisdictionLevelCode());

        article.setCaption("Legislation Reference Article");
        article.setValue(legislationReference.getArticle());

        URI.setCaption("Legislation Reference URI");
        URI.setValue(legislationReference.getURI());

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
