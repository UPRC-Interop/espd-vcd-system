/**
 * Created by ixuz on 2/2/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.CustomLayout;

/**
 * Sets up the base custom layout of the website.
 * Subclasses inheriting can add components to the sections exposed by this class.
 */
public class Master extends VerticalLayout {
    private Navigator navigator = null;
    private CustomLayout fullLayout = new CustomLayout("master");
    private Image logo = new Image("", new ThemeResource("img/logo.png"));

    // Exposed sections
    protected VerticalLayout content = new VerticalLayout();

    public Master(Navigator navigator) {
        this.navigator = navigator;

        logo.setSizeUndefined();
        content.setSizeFull();

        fullLayout.addComponent(logo, "logo");
        fullLayout.addComponent(content, "content");

        addComponent(fullLayout);
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
