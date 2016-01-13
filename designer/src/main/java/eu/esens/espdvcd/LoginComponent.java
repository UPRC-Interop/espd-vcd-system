/**
 * Created by ixuz on 1/13/16.
 */

package eu.esens.espdvcd;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Notification;

public class LoginComponent extends CustomComponent {

    private TextField usernameTextField;
    private PasswordField passwordPasswordField;
    private Button loginButton;

    public interface LoginListener {
        void callback(String username, String password);
    }

    public LoginComponent() {
        // Create a panel and set this panel as the root for this component
        Panel panel = new Panel("Login Component");
        setCompositionRoot(panel);

        // Create the layout for this component
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        panel.setContent(content);

        // Create username field
        usernameTextField = new TextField("Username");
        usernameTextField.setWidth("100%");
        usernameTextField.setInputPrompt("Write here!");
        content.addComponent(usernameTextField);

        // Create password field
        passwordPasswordField = new PasswordField("Password");
        passwordPasswordField.setWidth("100%");
        passwordPasswordField.setInputPrompt("Write here!");
        content.addComponent(passwordPasswordField);

        // Create login button
        loginButton = new Button("Login");
        loginButton.setWidth("100%");
        content.addComponent(loginButton);
    }

    public void setOnLoginCallback(final LoginListener loginListener) {
        getLoginButton().addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                String username = getUsernameTextField().getValue();
                String password = getPasswordPasswordField().getValue();
                loginListener.callback(username, password);
            }
        });
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
