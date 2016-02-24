/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer;

import com.vaadin.server.VaadinRequest;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.Theme;
import eu.esens.espdvcd.designer.views.*;

@Title("Designer")
@Theme("designertheme")

public class Designer extends UI {
    public final static String VIEW_INDEX  = "";
    public final static String VIEW_LOGIN  = "login";
    public final static String VIEW_SANDBOX  = "sandbox";

    @Override
    protected void init(VaadinRequest request) {

        Navigator navigator  = new Navigator(this, this);

        Start start = new Start(navigator);
        Login login = new Login(navigator);
        Sandbox sandbox = new Sandbox(navigator);

        navigator.addView(VIEW_INDEX, start);
        navigator.addView(VIEW_LOGIN, login);
        navigator.addView(VIEW_SANDBOX, sandbox);
    }
}
