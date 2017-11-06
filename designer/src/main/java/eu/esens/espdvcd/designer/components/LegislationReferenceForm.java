package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.codelist.CodeListsVersioner;
import eu.esens.espdvcd.model.LegislationReference;

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
        setWidth("100%");
        panel.setWidth("100%");
        addComponent(panel);
        panel.setContent(panelContent);
        panelContent.addComponent(title);
        panelContent.addComponent(description);
        panelContent.addComponent(jurisdictionLevelCode);
        panelContent.addComponent(article);
        panelContent.addComponent(URI);
             
        this.addLayoutClickListener(this::onLegislationReferenceClick);

        title.setCaption("Legislation Reference Title");
        title.setValue(legislationReference.getTitle());

        description.setCaption("Legislation Reference Description");
        description.setValue(legislationReference.getDescription());

        jurisdictionLevelCode.setCaption("Legislation Reference Jurisdiction Level Code");
        String legislationReferenceLevelDescription =  CodeListsVersioner.ForVersion1.CRITERION_JURISDICTION_LEVEL.getValueForId(legislationReference.getJurisdictionLevelCode());
        
        if (legislationReferenceLevelDescription == null) {
            legislationReferenceLevelDescription = legislationReference.getJurisdictionLevelCode();
        }
        jurisdictionLevelCode.setValue(legislationReferenceLevelDescription);

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
//        final Binder<LegislationReference> legislationReferenceGroup = new BeanValidationBinder<>(LegislationReference.class);
//        legislationReferenceGroup.bindInstanceFields(this);
//        legislationReferenceGroup.setBean(legislationReference);
    }

    void onLegislationReferenceClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof Panel && event.getClickedComponent() == panel) {
            if (!event.isDoubleClick()) {
                panelContent.setVisible(!panelContent.isVisible());

                if (panelContent.isVisible()) {
                    panel.setIcon(FontAwesome.CHEVRON_DOWN);
                } else {
                    panel.setIcon(FontAwesome.CHEVRON_RIGHT);
                }
            }
        }
    }
}
