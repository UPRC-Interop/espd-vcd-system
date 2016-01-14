package eu.esens.espdvcd;

import com.vaadin.server.VaadinRequest;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Notification;

import com.vaadin.navigator.View;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class Main extends VerticalLayout implements View {
    private Navigator navigator = null;
    public Main(Navigator navigator) {
        super();
        this.navigator = navigator;

        setSizeFull();
        setMargin(true);

        addComponent(new Button("Go to start!", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent e) {
                navigator.navigateTo(HelloWorld.STARTVIEW);
            }
        }));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        
    }
}
