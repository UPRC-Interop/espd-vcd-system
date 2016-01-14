package eu.esens.espdvcd;

/**
 * Created by ixuz on 1/14/16.
 */

import com.vaadin.ui.*;

public class RegisterFormWindow extends Window {

    private TextField usernameTextField;
    private PasswordField passwordPasswordField;
    private PasswordField passwordAgainPasswordField;
    private Button registerButton;

    public RegisterFormWindow() {
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

        // Password again text field
        passwordAgainPasswordField = new PasswordField("Password Again");
        content.addComponent(passwordAgainPasswordField);

        // Register button
        registerButton = new Button("Register");
        content.addComponent(registerButton);
    }
}
