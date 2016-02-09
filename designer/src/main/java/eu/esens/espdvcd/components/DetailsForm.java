package eu.esens.espdvcd.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.Designer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import eu.esens.espdvcd.FormFactory;
import eu.esens.espdvcd.model.uifacade.*;

/**
 * Created by ixuz on 2/2/16.
 */
public class DetailsForm extends CustomComponent {
    private Navigator navigator = null;
    private VerticalLayout layout;

    Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    Button next = new Button("Next", FontAwesome.ARROW_RIGHT);

    @SuppressWarnings("unchecked")
    public DetailsForm(Navigator navigator) {
        this.navigator = navigator;

        layout = new VerticalLayout();
        setCompositionRoot(layout);

        VerticalLayout formLayout = new VerticalLayout();

        ElementContainer<ElementContainer> espdTemplate = FormFactory.SampleEspdTemplate();
        for (ElementContainer<ElementContainer> element : espdTemplate.getDefaultContent()) {
            FormFactory.CreateForm(formLayout, element);
        }
        layout.addComponent(formLayout);

        // Create button list
        HorizontalLayout buttonList = new HorizontalLayout();
        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        previous.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                navigator.navigateTo(Designer.STARTVIEW);
            }
        });
        cancel.setEnabled(false);
        next.setEnabled(false);
        layout.addComponent(buttonList);
    }
}
