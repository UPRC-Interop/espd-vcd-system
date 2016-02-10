/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer;

import com.vaadin.server.VaadinRequest;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.Theme;
import eu.esens.espdvcd.designer.LoginFormWindow;
import eu.esens.espdvcd.designer.views.*;

@Title("Designer")
@Theme("designertheme")

public class Designer extends UI {
    public final static String VIEW_INDEX  = "";

    @Override
    protected void init(VaadinRequest request) {

        Navigator navigator  = new Navigator(this, this);

        Start start = new Start(navigator);

        navigator.addView(VIEW_INDEX, start);
    }
}
