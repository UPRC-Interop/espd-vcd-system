package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ModelBuilder;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.model.*;

public class Start extends Master {

    private static final long serialVersionUID = 2425920957238921278L;

    private final Label authInfoLabel = new Label("");
    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();
    private ESPDRequest espdRequest = null;

    public Start(Navigator navigator) {
        super(navigator, true);
        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);

        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);
        pagePanel.setSizeFull();

        pageContent.setStyleName("startPageContent");
        pageContent.setSizeFull();

        Label welcomeTitle = new Label("Welcome to the ESPD/VCD Designer");
        Label welcomeText = new Label("Please select an option from the menu on the left to proceed");

        welcomeTitle.setStyleName("pageCaptionLabel");
        welcomeText.setStyleName("startPageInfoText");
        welcomeTitle.setSizeUndefined();
        welcomeText.setSizeUndefined();

        pageContent.addComponent(welcomeTitle);
        pageContent.addComponent(welcomeText);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }
}
