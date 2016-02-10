/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.server.VaadinSession;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Label;
import com.vaadin.server.Page;
import eu.esens.espdvcd.designer.components.LoginFormWindow;
import eu.esens.espdvcd.designer.LoginManager;

public class Login extends Master implements View {
    private Navigator navigator = null;
    private LoginFormWindow loginFormWindow = new LoginFormWindow();

    public Login(Navigator navigator) {
        super(navigator);

        loginFormWindow.setOnLoginCallback((username, password) -> {
            // Mockup username and password verification
            if (username.equals("abc") && password.equals("123")) {
                LoginManager.setAuthenticated(true);
                Notification notification = new Notification("Login successful!");
                notification.setDelayMsec(2000);
                notification.show(Page.getCurrent());

                UI.getCurrent().removeWindow(loginFormWindow);
                getNavigator().navigateTo("");
            } else {
                Notification notification = new Notification("Login failed, please try again!");
                notification.setDelayMsec(2000);
                notification.show(Page.getCurrent());
            }
        });

        content.addComponent(new Label("You must login to use this service"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!LoginManager.isAuthenticated()) {
            UI.getCurrent().addWindow(loginFormWindow);
        } else {
            getNavigator().navigateTo("");
        }
    }
}
