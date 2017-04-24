package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.windows.CriterionWindow;

/**
 * Created by ixuz on 6/21/16.
 */
public class TestView extends VerticalLayout implements View {
    public TestView() {
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
