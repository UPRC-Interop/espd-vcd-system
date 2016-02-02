package eu.esens.espdvcd.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.Designer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ixuz on 2/2/16.
 */
public class DetailsForm extends CustomComponent {
    private Navigator navigator = null;
    private VerticalLayout layout;

    Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    Button next = new Button("Next", FontAwesome.ARROW_RIGHT);

    private static final List<String> countryCodes = new ArrayList<String>(Arrays.asList("SE", "NO", "FI", "DK"));

    public DetailsForm(Navigator navigator) {
        this.navigator = navigator;

        layout = new VerticalLayout();
        setCompositionRoot(layout);

        layout.addComponent(new Label("Identify of the procurer"));

        TextField nameTextfield = new TextField("Official Name");
        layout.addComponent(nameTextfield);

        ComboBox titleCombobox = new ComboBox("Country", countryCodes);
        layout.addComponent(titleCombobox);

        layout.addComponent(new Label("Information about the procurement procedure"));

        TextField titleTextfield = new TextField("Title");
        layout.addComponent(titleTextfield);

        TextField shortDescriptionTextfield = new TextField("Short description");
        layout.addComponent(shortDescriptionTextfield);

        TextField referenceTextfield = new TextField("File reference number attributed by the contracting authority or contracting entity (if applicable)");
        layout.addComponent(referenceTextfield);

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

