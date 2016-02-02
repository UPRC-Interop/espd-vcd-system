package eu.esens.espdvcd.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;

/**
 * Created by ixuz on 2/2/16.
 */
public class Master extends VerticalLayout {
    public VerticalLayout content = new VerticalLayout();

    public Master(Navigator navigator) {
        super();

        ThemeResource resource = new ThemeResource("img/logo.png");
        Image logo = new Image("", resource);
        addComponent(logo);

        content.setSizeUndefined();
        addComponent(content);

        setComponentAlignment(logo, Alignment.TOP_CENTER);
        setComponentAlignment(content, Alignment.TOP_CENTER);
    }
}
