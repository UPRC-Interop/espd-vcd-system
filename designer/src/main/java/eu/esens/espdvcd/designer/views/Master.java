/**
 * Created by ixuz on 2/2/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.Designer;
import eu.esens.espdvcd.designer.UserManager;
import com.vaadin.server.FontAwesome;

/**
 * Sets up the base custom layout of the website.
 * Subclasses inheriting can add components to the sections exposed by this class.
 */
public class Master extends VerticalLayout implements View {
    private Navigator navigator = null;
    private CustomLayout fullLayout = new CustomLayout("master");
    private boolean forceAuthentication = false;

    // Exposed sections
    protected HorizontalLayout topheader = new HorizontalLayout();
    protected VerticalLayout content = new VerticalLayout();
    protected Panel navigatorPanel = new Panel();
    protected VerticalLayout navigatorContent = new VerticalLayout();
    protected Panel mainPanel = new Panel();
    protected VerticalLayout mainContent = new VerticalLayout();
    protected Image topheaderEsensImage = new Image("", new ThemeResource("img/logo_esens.png"));
    protected Label topheaderWelcomeText = new Label("Hello Alice");

    public Master(Navigator navigator, boolean forceAuthentication) {
        this.navigator = navigator;
        this.forceAuthentication = forceAuthentication;

        setCaption(null);
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

        mainContent.setStyleName("master-mainContent");
        mainContent.setHeight("100%");
        mainContent.setWidth("100%");

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
            Button button = new Button("Start", FontAwesome.CIRCLE);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);

            button.addClickListener(this::onStart);
        }
        {
            Button button = new Button("Notifications", FontAwesome.THUMB_TACK);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("ESPD Templates", FontAwesome.FILE_O);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);

            button.addClickListener(this::onEspdTemplates);
        }
        {
            Button button = new Button("ESPDs", FontAwesome.FILE);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("Sandbox", FontAwesome.PLAY);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
            button.addClickListener(this::onSandbox);
        }
        {
            Button button = new Button("My Profile", FontAwesome.USER);
            button.setStyleName("navigatorButtonLight");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("Pre-fill Settings", FontAwesome.CHECK_SQUARE_O);
            button.setStyleName("navigatorButtonLight");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
        {
            Button button = new Button("Dashboard Settings", FontAwesome.GEAR);
            button.setStyleName("navigatorButtonLight");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
        }
    }

    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (forceAuthentication && !UserManager.isAuthenticated()) {
            getNavigator().navigateTo(Designer.VIEW_LOGIN);
        }
    }

    public void onStart(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_DASHBOARD);
    }

    public void onEspdTemplates(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_ESPD_TEMPLATES);
    }

    public void onSandbox(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_SANDBOX);
    }
}
