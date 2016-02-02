package eu.esens.espdvcd.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.Designer;

import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;

/**
 * Created by ixuz on 1/28/16.
 */
public class StartForm extends CustomComponent {
    private Navigator navigator = null;
    private VerticalLayout layout;

    Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    Button next = new Button("Next", FontAwesome.ARROW_RIGHT);

    private static final List<String> countryCodes = new ArrayList<String>(Arrays.asList("SE", "NO", "FI", "DK"));

    public StartForm(Navigator navigator) {
        this.navigator = navigator;

        layout = new VerticalLayout();
        setCompositionRoot(layout);

        Label descriptionTitleLabel = new Label("Welcome to the ESPD service");
        layout.addComponent(descriptionTitleLabel);

        //Label descriptionTextLabel = new Label("European Single Procurement Document (ESPD) is a self-declaration of the businesses' financial status, abilities and suitability for a public procurement procedure. It is available in all EU languages and used as a preliminary evidence of fulfilment of the conditions required in public procurement procedures across the EU. Thanks to the ESPD, the tenderers no longer have to provide full documentary evidence and different forms previously used in the EU procurement, which means a significant simplification of access to cross-border tendering opportunities. From October 2018 onwards the ESPD shall be provided exclusively in an electronic form. The European Commission provides a free web service for the buyers, bidders and other parties interested in filling in the ESPD electronically. The online form can be filled in, printed and then sent to the buyer together with the rest of the bid. If the procedure is run electronically, the ESPD can be exported, stored and submitted electronically. The ESPD provided in a previous public procurement procedure can be reused as long as the information remains correct. Bidders may be excluded from the procedure or be subject to prosecution if the information in the ESPD is seriously misrepresented, withheld or cannot be complemented with supporting documents.");
        //layout.addComponent(descriptionTextLabel);

        OptionGroup optionGroupWho = new OptionGroup("Who are you?");
        optionGroupWho.addItems(Arrays.asList("I am a contracting authority", "I am an economic operator"));
        layout.addComponent(optionGroupWho);

        OptionGroup optionGroupWhat = new OptionGroup("What would you like to do?");
        optionGroupWhat.addItems(Arrays.asList("Create a new ESPD", "Reuse an existing ESPD", "Review ESPD"));
        layout.addComponent(optionGroupWhat);

        ComboBox comboBox = new ComboBox("Where are you from?", countryCodes);
        layout.addComponent(comboBox);

        // Create button list
        HorizontalLayout buttonList = new HorizontalLayout();
        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        previous.setEnabled(false);
        cancel.setEnabled(false);
        next.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent e) {
                navigator.navigateTo(Designer.DETAILSVIEW);
            }
        });
        layout.addComponent(buttonList);
    }
}
