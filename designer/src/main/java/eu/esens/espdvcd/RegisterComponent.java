/**
 * Created by ixuz on 1/14/16.
 */

package eu.esens.espdvcd;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

public class RegisterComponent extends CustomComponent {

    private Button registerButton;

    public RegisterComponent() {
        // Create register button
        registerButton = new Button("Register");
        registerButton.setWidth("100%");
        registerButton.addClickListener((event) -> {
            // Create the register form window and open it in the UI
            RegisterFormWindow registerFormWindow = new RegisterFormWindow();
            UI.getCurrent().addWindow(registerFormWindow);
        });
        setCompositionRoot(registerButton);
    }
}
