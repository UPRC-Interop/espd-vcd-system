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
import com.vaadin.ui.Notification;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.Theme;

@Title("Almost something useful")
@Theme("valo")
public class HelloWorld extends UI {
    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout content = new VerticalLayout();

        content.setSizeFull();
        content.setMargin(true);

        content.addComponent(new Label("Hello, world!"));
        content.addComponent(new Button("Push me!", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent e) {
                Notification.show("Pushed!");
            }
        }));

        setContent(content);
    }
}
