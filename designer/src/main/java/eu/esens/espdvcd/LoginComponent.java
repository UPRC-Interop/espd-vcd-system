/**
 * Created by ixuz on 1/13/16.
 */

package eu.esens.espdvcd;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;

public class LoginComponent extends CustomComponent {

    private Button loginButton;
    private LoginFormWindow loginFormWindow;
    private LoginFormWindow.LoginListener loginListener;

    public LoginComponent() {
        // Create login button
        loginButton = new Button("Login");
        setCompositionRoot(loginButton);
        loginButton.setWidth("100%");
        loginButton.addClickListener((event) -> {
            // Create the login form window and open it in the UI
            loginFormWindow = new LoginFormWindow();
            if(loginListener != null)
                loginFormWindow.setOnLoginCallback(loginListener);

            UI.getCurrent().addWindow(loginFormWindow);
        });
    }

    public void setOnLoginCallback(final LoginFormWindow.LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
