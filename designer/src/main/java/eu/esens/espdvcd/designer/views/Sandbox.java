package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;

/**
 * Created by ixuz on 2/23/16.
 */

public class Sandbox extends Master implements View {
    private Navigator navigator = null;

    public Sandbox(Navigator navigator) {
        super(navigator);

        content.addComponent(new Label("This is the sandbox!"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
