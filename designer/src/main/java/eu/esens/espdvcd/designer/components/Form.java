/**
 * Created by ixuz on 2/9/16.
 */

package eu.esens.espdvcd.designer.components;

import eu.esens.espdvcd.designer.FormFactory;
import eu.esens.espdvcd.model.uifacade.ElementContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Button.ClickEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Form based upon an ElementContainer
 *
 * @see eu.esens.espdvcd.model.uifacade.ElementContainer
 */
public class Form extends CustomComponent {

    private VerticalLayout layout = new VerticalLayout();
    private VerticalLayout formLayout = new VerticalLayout();
    private HorizontalLayout buttonListLayout = new HorizontalLayout();
    private VerticalLayout progressLayout = new VerticalLayout();
    private Label progressLabel = new Label("");
    private ProgressBar progressBar = new ProgressBar(0.0f);
    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private Button finish = new Button("Export", FontAwesome.DOWNLOAD);
    private List<VerticalLayout> pages = new ArrayList<>();
    private int currentPageIndex = 0;

    /**
     * Constructs a fully functional Vaadin7 custom component that includes:
     * Pages, navigational buttons, progressbar and informational text
     * based upon a provided ElementContainer.
     *
     * @param espdTemplate
     * @see eu.esens.espdvcd.model.uifacade.ElementContainer
     */
    public Form(ElementContainer<ElementContainer> espdTemplate) {
        setCompositionRoot(layout);

        // Bind the click listeners to elements
        previous.addClickListener(this::onPrevious);
        cancel.addClickListener(this::onCancel);
        next.addClickListener(this::onNext);
        finish.addClickListener(this::onExport);

        // Progress layout
        progressBar.setWidth("100%");

        // Generate the form pages based on an existing model
        for (ElementContainer<ElementContainer> element : espdTemplate.getDefaultContent()) {
            VerticalLayout page = new VerticalLayout();
            FormFactory.CreateForm(page, element);
            pages.add(page);
            formLayout.addComponent(page);
        }

        //VerticalLayout page = new VerticalLayout();
        //FormFactory.CreateForm(page, espdTemplate);
        //pages.add(page);
        //formLayout.addComponent(page);

        // Compose the layout of this custom component
        progressLayout.addComponent(progressLabel);
        progressLayout.addComponent(progressBar);
        buttonListLayout.addComponent(previous);
        buttonListLayout.addComponent(cancel);
        buttonListLayout.addComponent(next);
        buttonListLayout.addComponent(finish);
        layout.addComponent(progressLayout);
        layout.addComponent(formLayout);
        layout.addComponent(buttonListLayout);

        // By default display the first page of the form
        displayPage(0);
    }

    /**
     * Displays page by index.
     *
     * @param pageIndex index of the page to display.
     */
    public void displayPage(int pageIndex) {
        // Make sure the requested page index is not out-of-bounds
        if (pageIndex < 0 || pageIndex >= pages.size()) {
            return;
        }

        // Find the requested page
        VerticalLayout page = pages.get(pageIndex);

        if (page == null) {
            return;
        }

        // Set all pages to be disabled and not visible
        for (VerticalLayout p : pages) {
            p.setEnabled(false);
            p.setVisible(false);
        }
        // Set only the specific page by index to be enabled and visible
        page.setEnabled(true);
        page.setVisible(true);

        // If the form is displaying the first page, hide the Previous button
        if (pageIndex == 0) {
            previous.setEnabled(false);
            previous.setVisible(false);
        } else {
            previous.setEnabled(true);
            previous.setVisible(true);
        }

        // If the form is displaying the last page, show the Export button and hide the Next button
        if (pageIndex == pages.size()-1) {
            finish.setEnabled(true);
            finish.setVisible(true);
            next.setEnabled(false);
            next.setVisible(false);
        } else {
            finish.setEnabled(false);
            finish.setVisible(false);
            next.setEnabled(true);
            next.setVisible(true);
        }

        // Refresh the progress label and progress bar
        progressLabel.setValue("You've completed " + currentPageIndex + " of " + pages.size() + " pages");
        progressBar.setValue((float)currentPageIndex / pages.size());
    }

    /**
     * Flips to the next page of the form.
     * If there is no next page, this method will have no effect.
     */
    public void displayNextPage() {
        currentPageIndex++;
        if (currentPageIndex >= pages.size()) {
            currentPageIndex = pages.size()-1;
            return;
        }
        displayPage(currentPageIndex);
    }

    /**
     * Flips to the previous page of the form.
     * If there is no previous page, this method will have no effect.
     */
    public void displayPreviousPage() {
        currentPageIndex--;
        if (currentPageIndex < 0) {
            currentPageIndex = 0;
            return;
        }
        displayPage(currentPageIndex);
    }

    /**
     * When the user have clicked the Previous button, this method is invoked.
     * Displays the previous page of the form.
     * If there is no previous page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onPrevious(ClickEvent event) {
        displayPreviousPage();
    }

    /**
     * When the user have clicked the Cancel button, this method is invoked.
     * TODO: Implement logic for restarting/leaving the form page.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onCancel(ClickEvent event) {
        System.out.println("Cancel process");
    }

    /**
     * When the user have clicked the Next button, this method is invoked.
     * Displays the next page of the form.
     * If there is no next page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onNext(ClickEvent event) {
        displayNextPage();
    }

    /**
     * When the user have clicked the Export button, this method is invoked.
     * TODO: Implement logic for exporting all form data to xml.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onExport(ClickEvent event) {
        System.out.println("Export xml");
    }
}
