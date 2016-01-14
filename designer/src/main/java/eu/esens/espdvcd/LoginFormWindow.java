package eu.esens.espdvcd;

/**
 * Created by ixuz on 1/14/16.
 */

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;

public class LoginFormWindow extends Window {

    private TextField usernameTextField;
    private PasswordField passwordPasswordField;
    private Button loginButton;
    private ClickListener clickListener;

    public interface LoginListener {
        void callback(String username, String password);
    }

    public LoginFormWindow() {
        // Create a sub-window and set the content
        this.center();
        this.setModal(true);

        // Create the layout for this window
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        this.setContent(content);

        // Username text field
        usernameTextField = new TextField("Username");
        content.addComponent(usernameTextField);

        // Password text field
        passwordPasswordField = new PasswordField("Password");
        content.addComponent(passwordPasswordField);

        // Login button
        loginButton = new Button("Login");
        content.addComponent(loginButton);

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
