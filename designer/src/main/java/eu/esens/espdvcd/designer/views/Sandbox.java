package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Arrays;
import java.util.Date;

import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.designer.sandbox.PojoContainer;
import eu.esens.espdvcd.designer.sandbox.ResponseDataDateForm;
import eu.esens.espdvcd.designer.sandbox.PojoDate;

/**
 * Created by ixuz on 2/23/16.
 */

public class Sandbox extends Master {

    private static final long serialVersionUID = -3712031177853996322L;

    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();

    public Sandbox(Navigator navigator) {
        super(navigator, false);

        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);

        pageContent.setMargin(true);
        pageContent.setSpacing(true);

        // INDICATOR
        pageContent.addComponent(new Label("INDICATOR"));
        pageContent.addComponent(new CheckBox("Yes/No"));

        // DESCRIPTION/PERIOD_DESCRIPTION/EVIDENCE_URL/CODE
        pageContent.addComponent(new Label("DESCRIPTION/PERIOD_DESCRIPTION/EVIDENCE_URL/CODE"));
        pageContent.addComponent(new TextField("Text"));

        // AMOUNT
        pageContent.addComponent(new Label("AMOUNT"));
        pageContent.addComponent(new ComboBox("Select currency", Arrays.asList("EUR", "SEK")));
        pageContent.addComponent(new TextField("Amount"));

        // COUNTRY
        pageContent.addComponent(new Label("COUNTRY"));
        pageContent.addComponent(new CountryComboBox("Select country", Arrays.asList("Sweden", "Germany", "Greece")));

        // DATE
        pageContent.addComponent(new Label("DATE"));

        PojoContainer<?> pojoContainer = new PojoContainer<>(new PojoDate(new Date()));
        if (pojoContainer.getPojo() instanceof PojoDate) {
            PojoDate pojoDate = (PojoDate) pojoContainer.getPojo();
            ResponseDataDateForm responseDataDateForm = new ResponseDataDateForm(pojoDate);
            pageContent.addComponent(responseDataDateForm);
        }

        // Just a button that prints stuff
        Button printButton = new Button("Print stuff");
        printButton.addClickListener(this::onPrint);
        pageContent.addComponent(printButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    private void onPrint(Button.ClickEvent event) {
        /*if (pojoContainer.getPojo() instanceof PojoDate) {
            PojoDate pojoDate = (PojoDate) pojoContainer.getPojo();
            PojoDate responseDataDate = (PojoDate) pojoDate;
            System.out.println("Print: " + responseDataDate.getDate());
        }*/
    }
}
