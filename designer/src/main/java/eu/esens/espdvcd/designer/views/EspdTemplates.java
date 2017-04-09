package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Grid;

public class EspdTemplates extends Master {

    private Grid table = new Grid();

    public EspdTemplates(Navigator navigator) {
        super(navigator, true);

     }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }
}
