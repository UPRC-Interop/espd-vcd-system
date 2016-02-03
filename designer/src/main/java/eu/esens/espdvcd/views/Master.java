package eu.esens.espdvcd.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button;

/**
 * Created by ixuz on 2/2/16.
 */
public class Master extends VerticalLayout {
    CustomLayout fullLayout = new CustomLayout("master");
    public VerticalLayout content = new VerticalLayout();
    Image logo = new Image("", new ThemeResource("img/logo.png"));

    public Master(Navigator navigator) {
        super();

        fullLayout.addComponent(logo, "logo");
        fullLayout.addComponent(content, "content");

        logo.setSizeUndefined();
        content.setSizeFull();

        addComponent(fullLayout);
    }
}
