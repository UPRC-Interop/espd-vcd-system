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

    public DetailsForm(Navigator navigator) {
        this.navigator = navigator;

        layout = new VerticalLayout();
        setCompositionRoot(layout);

        VerticalLayout formLayout = new VerticalLayout();
        FormFactory.CreateForm(formLayout, FormFactory.SampleEspdTemplate());

        layout.addComponent(formLayout);

        layout.addComponent(new Label("Identify of the procurer"));

        TextField nameTextfield = new TextField("Official Name");
        nameTextfield.setStyleName("box_input");
        nameTextfield.setInputPrompt("Write official name");
        layout.addComponent(nameTextfield);

        CountryComboBox countryComboBox = new CountryComboBox("Country", "Sweden");
        countryComboBox.setStyleName("box_input");
        layout.addComponent(countryComboBox);

        layout.addComponent(new Label("Information about the procurement procedure"));

        TextField titleTextfield = new TextField("Title");
        titleTextfield.setStyleName("box_input");
        titleTextfield.setInputPrompt("Write title");
        layout.addComponent(titleTextfield);

        TextField shortDescriptionTextfield = new TextField("Short description");
        shortDescriptionTextfield.setStyleName("box_input");
        shortDescriptionTextfield.setInputPrompt("Write description");
        layout.addComponent(shortDescriptionTextfield);

        TextField referenceTextfield = new TextField("File reference number attributed by the contracting authority or contracting entity (if applicable)");
        referenceTextfield.setStyleName("box_input");
        referenceTextfield.setInputPrompt("Write file reference");
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

