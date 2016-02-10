/**
 * Created by ixuz on 2/2/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.CustomLayout;
import eu.esens.espdvcd.designer.LoginManager;

public class Master extends VerticalLayout {
    private Navigator navigator = null;
    CustomLayout fullLayout = new CustomLayout("master");
    public VerticalLayout content = new VerticalLayout();
    Image logo = new Image("", new ThemeResource("img/logo.png"));

    public Master(Navigator navigator) {
        this.navigator = navigator;

        fullLayout.addComponent(logo, "logo");
        fullLayout.addComponent(content, "content");

        logo.setSizeUndefined();
        content.setSizeFull();

        addComponent(fullLayout);
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
