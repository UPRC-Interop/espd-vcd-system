/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd;

import com.vaadin.server.VaadinRequest;

import com.vaadin.ui.UI;

import com.vaadin.navigator.Navigator;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.Theme;
import eu.esens.espdvcd.views.Start;
import eu.esens.espdvcd.views.Details;

@Title("Designer")
@Theme("designertheme")

public class Designer extends UI {
    public final static String STARTVIEW  = "";
    public final static String DETAILSVIEW = "details";

    @Override
    protected void init(VaadinRequest request) {
        Navigator navigator  = new Navigator(this, this);

        Start start = new Start(navigator);
        Details details  = new Details(navigator);

        navigator.addView(STARTVIEW, start);
        navigator.addView(DETAILSVIEW, details);
    }
}
