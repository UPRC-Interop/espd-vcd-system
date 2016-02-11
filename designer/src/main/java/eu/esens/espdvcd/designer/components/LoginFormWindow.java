/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.TextField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Label;
import com.vaadin.ui.CustomLayout;

public class LoginFormWindow extends Window {

    private VerticalLayout layout;
    private CustomLayout customLayout;
    private Label infoLabel;
    private TextField usernameTextField;
    private PasswordField passwordPasswordField;
    private Button loginButton;
    private ClickListener clickListener;

    public interface LoginListener {
        void callback(String username, String password);
    }

    public LoginFormWindow() {

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
        setOnLoginCallback((username, password) -> {
            Notification.show("Warning: No login callback!");
        });
    }

    public void setOnLoginCallback(final LoginListener loginListener) {
        getLoginButton().removeClickListener(clickListener);
        clickListener = (event) -> {
            String username = getUsernameTextField().getValue();
            String password = getPasswordPasswordField().getValue();
            loginListener.callback(username, password);
        };
        getLoginButton().addClickListener(this.clickListener);
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public PasswordField getPasswordPasswordField() {
        return passwordPasswordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}
