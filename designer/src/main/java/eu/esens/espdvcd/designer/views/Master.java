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
    protected Panel detailsPanel = new Panel();
    protected VerticalLayout detailsContent = new VerticalLayout();

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

        GridLayout contentGrid = new GridLayout(3,1);
        contentGrid.setWidth("100%");
        contentGrid.setHeight("100%");
        contentGrid.setStyleName("contentGrid");
        contentGrid.setColumnExpandRatio(0, 0.16f);
        contentGrid.setColumnExpandRatio(1, 0.62f);
        contentGrid.setColumnExpandRatio(2, 0.22f);

        navigatorContent.setStyleName("navigatorContent");
        navigatorContent.setHeight("100%");
        //navigatorContent.setSizeFull();


        navigatorPanel.setStyleName("navigatorPanel");
        navigatorPanel.setSizeFull();




        navigatorPanel.setContent(navigatorContent);
        navigatorPanel.getContent().setSizeUndefined();
        //navigatorPanel.getContent().setSizeFull();
        navigatorPanel.getContent().setWidth("100%");


        mainContent.setStyleName("master-mainContent");
        mainContent.setHeight("100%");
        mainContent.setWidth("100%");

        mainPanel.setStyleName("mainPanel");
        mainPanel.setSizeFull();
        mainPanel.setContent(mainContent);
        mainPanel.getContent().setSizeUndefined();

        detailsPanel.setStyleName("detailsPanel");
        detailsPanel.setSizeFull();
        detailsPanel.setContent(detailsContent);
        detailsPanel.getContent().setSizeUndefined();

        //detailsContent.setHeight("100%");
        detailsContent.setStyleName("master-detailsContent");
        detailsContent.setWidth("100%");

        contentGrid.addComponent(navigatorPanel, 0, 0);
        contentGrid.addComponent(mainPanel, 1, 0);
        contentGrid.addComponent(detailsPanel, 2, 0);
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
            Button button = new Button("ESPD Template", FontAwesome.FILE_O);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);

            button.addClickListener(this::onNavigatorEspdTemplate);
        }
        {
            Button button = new Button("ESPD", FontAwesome.FILE);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);

            button.addClickListener(this::onNavigatorEspd);
        }
        {
            Button button = new Button("Sandbox", FontAwesome.PLAY);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
            button.addClickListener(this::onNavigatorSandbox);
        }
        {
            Button button = new Button("Log-out", FontAwesome.ARROW_LEFT);
            button.setStyleName("navigatorButtonDark");
            button.setWidth("100%");
            button.setHeight(60, Unit.PIXELS);
            navigatorContent.addComponent(button);
            button.addClickListener(this::onNavigatorLogout);
        }
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public VerticalLayout getNavigatorContent() {
        return navigatorContent;
    }

    public VerticalLayout getMainContent() {
        return mainContent;
    }

    public VerticalLayout getDetailsContent() {
        return detailsContent;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (forceAuthentication && !UserManager.isAuthenticated()) {
            getNavigator().navigateTo(Designer.VIEW_LOGIN);
        }
    }

    public void onNavigatorEspdTemplate(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_ESPD_TEMPLATE);
    }

    public void onNavigatorEspd(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_ESPD);
    }

    public void onNavigatorSandbox(Button.ClickEvent clickEvent) {
        getNavigator().navigateTo(Designer.VIEW_SANDBOX);
    }

    public void onNavigatorLogout(Button.ClickEvent clickEvent) {
        UserManager.logout();
        getNavigator().navigateTo(Designer.VIEW_LOGIN);
    }
}
