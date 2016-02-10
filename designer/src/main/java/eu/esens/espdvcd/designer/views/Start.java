/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import eu.esens.espdvcd.Form;
import eu.esens.espdvcd.FormFactory;

public class Start extends Master implements View {
    private Navigator navigator = null;
    public Start(Navigator navigator) {
        this.navigator = navigator;

        Form form = new Form(FormFactory.SampleEspdTemplate());
        content.addComponent(form);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        
    }
}
