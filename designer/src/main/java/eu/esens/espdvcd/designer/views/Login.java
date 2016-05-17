/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.designer.Designer;
import eu.esens.espdvcd.designer.components.LoginFormWindow;
import eu.esens.espdvcd.designer.UserManager;

public class Login extends Master {
    private Navigator navigator = null;
    private LoginFormWindow loginFormWindow;

    public Login(Navigator navigator) {
        super(navigator, false);

        loginFormWindow = new LoginFormWindow(navigator);

        mainContent.addComponent(new Label("You must login to use this service"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!UserManager.isAuthenticated()) {
            UI.getCurrent().addWindow(loginFormWindow);
        } else {
            getNavigator().navigateTo(Designer.VIEW_ESPD_TEMPLATE);
        }
    }
}
