/**
 * Created by ixuz on 2/2/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.UserContext;
import eu.esens.espdvcd.designer.UserManager;
import com.vaadin.server.Sizeable;

/**
 * Sets up the base custom layout of the website.
 * Subclasses inheriting can add components to the sections exposed by this class.
 */
public class Master extends VerticalLayout {
    private Navigator navigator = null;
    private CustomLayout fullLayout = new CustomLayout("master");
    private Image topheaderEsensImage = new Image("", new ThemeResource("img/logo_esens.png"));
    Label topheaderWelcomeText = new Label("Hello Alice");

    // Exposed sections
    protected HorizontalLayout topheader = new HorizontalLayout();
    protected VerticalLayout content = new VerticalLayout();
    protected Panel navigatorPanel = new Panel();
    protected VerticalLayout navigatorContent = new VerticalLayout();
    protected Panel mainPanel = new Panel();
    protected VerticalLayout mainContent = new VerticalLayout();

    public Master(Navigator navigator) {
        this.navigator = navigator;

        setStyleName("Master");
        setHeight("100%");

        addComponent(fullLayout);

        fullLayout.addComponent(topheader, "topheader");
        fullLayout.addComponent(content, "content");

        topheader.addComponent(topheaderEsensImage);

        fullLayout.setStyleName("fullLayout");
        fullLayout.setHeight("100%");

        topheader.setStyleName("topheader");
        topheader.setWidth("100%");
        topheader.addComponent(topheaderWelcomeText);

        topheader.setHeight(76, Unit.PIXELS);
        topheader.setComponentAlignment(topheaderEsensImage, Alignment.MIDDLE_LEFT);
        topheader.setComponentAlignment(topheaderWelcomeText, Alignment.BOTTOM_RIGHT);

        topheaderEsensImage.setStyleName("topheaderEsensImage");
        topheaderEsensImage.setCaption(null);
        topheaderEsensImage.setWidth(150, Unit.PIXELS);

        topheaderWelcomeText.setStyleName("topheaderWelcomeText");
        topheaderWelcomeText.setSizeUndefined();

        content.setStyleName("content");
        content.setHeight("100%");

        GridLayout contentGrid = new GridLayout(2,1);
        contentGrid.setWidth("100%");
        contentGrid.setHeight("100%");
        contentGrid.setStyleName("contentGrid");
        contentGrid.setColumnExpandRatio(0, 0.17f);
        contentGrid.setColumnExpandRatio(1, 0.83f);

        navigatorContent.setStyleName("navigatorContent");
        navigatorContent.setHeight("100%");

        navigatorPanel.setStyleName("navigatorPanel");
        navigatorPanel.setSizeFull();
        navigatorPanel.setContent(navigatorContent);
        navigatorPanel.getContent().setSizeUndefined();
        navigatorPanel.getContent().setWidth("100%");

        mainContent.setStyleName("mainContent");
        mainContent.setHeight("100%");

        mainPanel.setStyleName("mainPanel");
        mainPanel.setSizeFull();
        mainPanel.setContent(mainContent);
        mainPanel.getContent().setSizeUndefined();

        contentGrid.addComponent(navigatorPanel, 0, 0);
        contentGrid.addComponent(mainPanel, 1, 0);

        content.addComponent(contentGrid);

        {
            VerticalLayout navigatorHeaderLayout = new VerticalLayout();
            navigatorHeaderLayout.setStyleName("navigatorHeaderLayout");

            Label labelTitle = new Label("Dashboard");
            labelTitle.setStyleName("navigatorHeaderTitle");
            labelTitle.setSizeUndefined();
            navigatorHeaderLayout.addComponent(labelTitle);
            navigatorHeaderLayout.setComponentAlignment(labelTitle, Alignment.TOP_CENTER);

            Label labelRole = new Label("Contracting Authority");
            labelRole.setStyleName("navigatorHeaderRole");
            labelRole.setSizeUndefined();
            navigatorHeaderLayout.addComponent(labelRole);
            navigatorHeaderLayout.setComponentAlignment(labelRole, Alignment.TOP_CENTER);

            navigatorContent.addComponent(navigatorHeaderLayout);
        }
        {
            Button button = new Button("Start");
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("Notifications");
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("ESPD Templates");
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("ESPDs");
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("My Profile");
            button.setStyleName("navigatorButtonLight");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("Pre-fill Settings");
            button.setStyleName("navigatorButtonLight");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("Dashboard Settings");
            button.setStyleName("navigatorButtonLight");
            button.setWidth("100%");
            button.setHeight(50, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }

/*
        setSizeFull();
        setHeight("100%");

        topheaderEsensImage.setSizeUndefined();

        // Top header bar
        fullLayout.setHeight("100%");
        fullLayout.setStyleName("fullLayout");
        fullLayout.addComponent(topheader, "topheader");
        fullLayout.addComponent(content, "content");

        // Setup the topheader layout
        topheader.setStyleName("topheader");
        topheader.setSizeFull();
        // Setup the grid layout of the top header
        GridLayout topheaderGrid = new GridLayout(2, 1);
        topheaderGrid.setStyleName("topheaderGrid");
        topheaderGrid.setWidth("100%");
        // Add the logo to the topheader
        topheaderEsensImage.setStyleName("topheaderEsensImage");
        topheaderEsensImage.setCaption(null);
        topheaderEsensImage.setWidth(200, Unit.PIXELS);
        topheaderGrid.addComponent(topheaderEsensImage, 0, 0);
        topheaderGrid.setComponentAlignment(topheaderEsensImage, Alignment.MIDDLE_LEFT);
        // Add the welcome text to the topheader
        Label topheaderWelcomeText = new Label("Hello Alice");
        topheaderWelcomeText.setStyleName("topheaderWelcomeText");
        topheaderWelcomeText.setSizeUndefined();
        topheaderGrid.addComponent(topheaderWelcomeText, 1, 0);
        topheaderGrid.setComponentAlignment(topheaderWelcomeText, Alignment.MIDDLE_RIGHT);
        topheader.addComponent(topheaderGrid);

        // Setup the content layout
        content.setSizeFull();
        content.setHeight("100%");
        // Setup the grid layout of the main content
        GridLayout contentGrid = new GridLayout(2, 1);
        contentGrid.setStyleName("contentGrid");
        contentGrid.setColumnExpandRatio(0, 0.17f);
        contentGrid.setColumnExpandRatio(1, 0.83f);
        contentGrid.setSizeFull();
        contentGrid.setWidth("100%");
        contentGrid.setHeight("100%");
        // Setup the side navigator
        VerticalLayout contentNavLayout = new VerticalLayout();
        contentNavLayout.setStyleName("contentNavLayout");
        contentNavLayout.setSizeFull();
        contentGrid.addComponent(contentNavLayout, 0, 0);
        contentGrid.setComponentAlignment(contentNavLayout, Alignment.TOP_LEFT);
        // Setup the main content view
        VerticalLayout contentMainLayout = new VerticalLayout();
        contentMainLayout.setStyleName("contentMainLayout");
        contentMainLayout.setSizeFull();
        contentGrid.addComponent(contentMainLayout, 1, 0);
        contentGrid.setComponentAlignment(contentMainLayout, Alignment.TOP_LEFT);
        content.addComponent(contentGrid);

        contentNavLayout.addComponent(new Label("Hello"));

        // Add button to side navigation pane
        Button contentNavNotificationsButton = new Button("Notifications");
        contentNavNotificationsButton.setSizeFull();
        contentNavLayout.addComponent(contentNavNotificationsButton);

        // Add button to side navigation pane
        Button contentNavESPDTemplatesButton = new Button("ESPD Templates");
        contentNavESPDTemplatesButton.setSizeFull();
        contentNavLayout.addComponent(contentNavESPDTemplatesButton);

        // Add button to side navigation pane
        Button contentNavESPDsButton = new Button("ESPDs");
        contentNavESPDsButton.setSizeFull();
        contentNavLayout.addComponent(contentNavESPDsButton);

        contentMainLayout.addComponent(new Label("World"));

        addComponent(fullLayout);
        setExpandRatio(fullLayout, 1.0f);*/
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
