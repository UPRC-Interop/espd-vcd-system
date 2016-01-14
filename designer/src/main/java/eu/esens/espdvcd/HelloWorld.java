/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd;

import com.vaadin.server.VaadinRequest;

import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Notification;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.Theme;

@Title("Almost something useful")
@Theme("valo")

public class HelloWorld extends UI {
    public final static String MAINVIEW  = "";
    public final static String STARTVIEW = "start";

    @Override
    protected void init(VaadinRequest request) {
        /*VerticalLayout content = new VerticalLayout();

        content.setSizeFull();
        content.setMargin(true);

        content.addComponent(new Label("Hello, world!"));
        content.addComponent(new Button("Push me!", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent e) {
                Notification.show("Pushed!");
            }
        }));

        // Add the login component
        LoginComponent loginComponent = new LoginComponent();
        loginComponent.setOnLoginCallback((username, password) -> {
            // Mockup username and password verification
            if (username.equals("abc") && password.equals("123")) {
                Notification.show("Login successful!");
            } else {
                Notification.show("Login failed!");
            }
        });*/


        Navigator navigator  = new Navigator(this, this);

        Start start = new Start(navigator);
        Main  main  = new Main(navigator);

        navigator.addView(MAINVIEW,  main);
        navigator.addView(STARTVIEW, start);

        // Add the register component
        RegisterComponent registerComponent = new RegisterComponent();
        content.addComponent(registerComponent);

        // Apply the contents to the UI
        //setContent(content);
    }
}
