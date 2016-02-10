/**
 * Created by ixuz on 2/2/16.
 */

package eu.esens.espdvcd.views;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.CustomLayout;

public class Master extends VerticalLayout {
    CustomLayout fullLayout = new CustomLayout("master");
    public VerticalLayout content = new VerticalLayout();
    Image logo = new Image("", new ThemeResource("img/logo.png"));

    public Master() {
        fullLayout.addComponent(logo, "logo");
        fullLayout.addComponent(content, "content");

        logo.setSizeUndefined();
        content.setSizeFull();

        addComponent(fullLayout);
    }
}
