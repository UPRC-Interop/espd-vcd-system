package eu.esens.espdvcd.views;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.navigator.View;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import eu.esens.espdvcd.components.StartForm;

public class Start extends Master implements View {
    private Navigator navigator = null;
    public Start(Navigator navigator) {
        super(navigator);
        this.navigator = navigator;

        StartForm startForm = new StartForm(navigator);
        content.addComponent(startForm);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        
    }
}
