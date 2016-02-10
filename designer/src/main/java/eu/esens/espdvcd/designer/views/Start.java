/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import eu.esens.espdvcd.designer.components.Form;
import eu.esens.espdvcd.designer.FormFactory;
import eu.esens.espdvcd.designer.LoginManager;

public class Start extends Master implements View {
    private Navigator navigator = null;
    public Start(Navigator navigator) {
        super(navigator);

        Form form = new Form(FormFactory.SampleEspdTemplate());
        content.addComponent(form);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!LoginManager.isAuthenticated()) {
            getNavigator().navigateTo("login");
        }
    }
}
