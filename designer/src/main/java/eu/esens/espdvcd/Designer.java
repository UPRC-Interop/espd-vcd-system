/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import com.vaadin.navigator.Navigator;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import eu.esens.espdvcd.model.uifacade.ElementContainer;
import eu.esens.espdvcd.views.Master;
import eu.esens.espdvcd.views.Start;
import eu.esens.espdvcd.views.Details;

@Title("Designer")
@Theme("designertheme")

public class Designer extends UI {
    public final static String STARTVIEW  = "";
    public final static String DETAILSVIEW = "details";

    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private static Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private static Button finish = new Button("Export", FontAwesome.DOWNLOAD);

    private static List<VerticalLayout> formPages = new ArrayList<VerticalLayout>();
    static int formIndex = 0;
    private static Label progressLabel = new Label("");
    private static ProgressBar progressBar = new ProgressBar(0.0f);

    @Override
    protected void init(VaadinRequest request) {
        Master master = new Master();
        setContent(master);

        VerticalLayout progressbarLayout = new VerticalLayout();
        progressbarLayout.addComponent(progressLabel);
        progressBar.setWidth("100%");
        progressbarLayout.addComponent(progressBar);
        master.content.addComponent(progressbarLayout);

        formPages.clear();
        ElementContainer<ElementContainer> espdTemplate = FormFactory.SampleEspdTemplate();
        for (ElementContainer<ElementContainer> element : espdTemplate.getDefaultContent()) {
            VerticalLayout form = new VerticalLayout();
            FormFactory.CreateForm(form, element);
            formPages.add(form);
            master.content.addComponent(form);
        }

        // Add the horizontal button list
        HorizontalLayout buttonList = new HorizontalLayout();
        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        buttonList.addComponent(finish);

        previous.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                displayPreviousFormPage();
            }
        });
        cancel.setEnabled(false);
        next.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent e) {
                displayNextFormPage();
            }
        });
        finish.setVisible(false);
        master.content.addComponent(buttonList);

        displayFormPage(formIndex);
    }

    public static void displayFormPage(int pageIndex) {
        if (formPages.size() == 0)
            return;

        if (pageIndex < 0 || pageIndex >= formPages.size())
            return;

        for (VerticalLayout form : formPages) {
            form.setVisible(false);
        }
        formPages.get(pageIndex).setVisible(true);
        progressLabel.setValue("You've completed " + pageIndex + " out of " + formPages.size() + " pages");
        progressBar.setValue((float)formIndex / formPages.size());

        if (formIndex == formPages.size()-1) {
            next.setVisible(false);
            finish.setVisible(true);
        } else {
            next.setVisible(true);
            finish.setVisible(false);
        }
    }

    public static void displayNextFormPage() {
        if (formIndex < formPages.size()-1) {
            formIndex++;
            displayFormPage(formIndex);
        }
    }

    public static void displayPreviousFormPage() {
        if (formIndex > 0) {
            formIndex--;
            displayFormPage(formIndex);
        }
    }
}
