package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.Designer;

/**
 * Created by ixuz on 3/4/16.
 */
public class Dashboard extends Master {

    Button newEspdTemplateButton = new Button("Create new ESPD Template!");

    public Dashboard(Navigator navigator) {
        super(navigator, true);

        mainContent.addComponent(newEspdTemplateButton);

        newEspdTemplateButton.setStyleName("dashboard-newEspdTemplateButton");
        newEspdTemplateButton.addClickListener(this::onNewEspdTemplates);
        newEspdTemplateButton.setWidth(400, Unit.PIXELS);
        newEspdTemplateButton.setHeight(160, Unit.PIXELS);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    public void onNewEspdTemplates(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_INDEX);
    }
}
