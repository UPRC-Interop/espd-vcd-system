/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import eu.esens.espdvcd.designer.Designer;
import eu.esens.espdvcd.designer.UserManager;

public class LoginFormWindow extends Window {

    private Navigator navigator;
    private VerticalLayout layout;
    private CustomLayout customLayout;
    private Label infoLabel;
    private TextField usernameTextField;
    private PasswordField passwordPasswordField;
    private Button loginButton;

    public LoginFormWindow(Navigator navigator) {
        this.navigator = navigator;

        // Create a sub-window and set the layout
        this.center();
        this.setClosable(false);
        this.setDraggable(false);
        this.setResizable(false);
        this.setModal(true);

        // Create the layout for this window
        layout = new VerticalLayout();
        this.setContent(layout);
        layout.setMargin(true);

        customLayout = new CustomLayout("loginForm");
        layout.addComponent(customLayout);

        // Login information field
        infoLabel = new Label("Welcome, please login");
        infoLabel.setStyleName("login-info-text");
        customLayout.addComponent(infoLabel, "info");

        // Username text field
        usernameTextField = new TextField("");
        usernameTextField.setInputPrompt("Username");
        usernameTextField.setStyleName("login-username-field");
        customLayout.addComponent(usernameTextField, "username");

        // Password text field
        passwordPasswordField = new PasswordField("");
        passwordPasswordField.setInputPrompt("Password");
        passwordPasswordField.setStyleName("login-password-field");
        customLayout.addComponent(passwordPasswordField, "password");

        // Login button
        loginButton = new Button("Login");
        loginButton.setStyleName("login-login-button");
        customLayout.addComponent(loginButton, "login");

        // Set the default login callback
        loginButton.addClickListener(this::onLogin);
    }

    public void onLogin(ClickEvent clickEvent) {
        String username = usernameTextField.getValue();
        String password = passwordPasswordField.getValue();
        usernameTextField.clear();
        passwordPasswordField.clear();

        if (UserManager.login(username, password) != null) {
            Notification notification = new Notification("Login successful!");
            notification.setPosition(Position.TOP_CENTER);
            notification.setDelayMsec(1000);
            notification.show(Page.getCurrent());

            UI.getCurrent().removeWindow(this);
            navigator.navigateTo(Designer.VIEW_ESPD_TEMPLATE);
        } else {
            Notification notification = new Notification("Login failed, please try again!");
            notification.setPosition(Position.TOP_CENTER);
            notification.setDelayMsec(1000);
            notification.show(Page.getCurrent());
        }
    }
}
