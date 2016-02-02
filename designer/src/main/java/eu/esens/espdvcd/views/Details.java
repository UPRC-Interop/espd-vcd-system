package eu.esens.espdvcd.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.components.DetailsForm;

/**
 * Created by ixuz on 2/2/16.
 */

public class Details extends Master implements View {
    private Navigator navigator = null;
    public Details(Navigator navigator) {
        super(navigator);
        this.navigator = navigator;

        DetailsForm detailsForm = new DetailsForm(navigator);
        content.addComponent(detailsForm);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
