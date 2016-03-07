package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.model.Criteria;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.SelectableCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ixuz on 3/7/16.
 */
public class ESPDRequestForm extends VerticalLayout {

    private VerticalLayout page1 = new VerticalLayout();
    private VerticalLayout page2 = new VerticalLayout();
    private VerticalLayout page3 = new VerticalLayout();
    private List<VerticalLayout> pages = new ArrayList<>();
    private HorizontalLayout buttonList = new HorizontalLayout();
    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private Button finish = new Button("Export", FontAwesome.DOWNLOAD);
    private int currentPageIndex = 0;

    public ESPDRequestForm(ESPDRequest espdRequest) {
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        for (VerticalLayout page : pages) {
            addComponent(page);
        }
        addComponent(buttonList);
        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        buttonList.addComponent(finish);
        previous.addClickListener(this::onPrevious);
        cancel.addClickListener(this::onCancel);
        next.addClickListener(this::onNext);
        finish.addClickListener(this::onExport);

        showPage(currentPageIndex);

        CADetailsForm caDetailsForm = new CADetailsForm(espdRequest);
        page1.addComponent(caDetailsForm);

        for (SelectableCriterion criterion : espdRequest.getCriterionList()) {
            page2.addComponent(new CriteriaForm(criterion));
        }
    }

    private void showPage(int pageIndex) {
        for (int i=0; i<pages.size(); i++) {
            VerticalLayout page = pages.get(i);
            if (i == pageIndex) {
                page.setVisible(true);
            } else {
                page.setVisible(false);
            }
        }
    }

    /**
     * Displays the previous page of the form.
     * If there is no previous page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onPrevious(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex-1 >= 0 ? currentPageIndex-1 : currentPageIndex);
        showPage(currentPageIndex);
        System.out.println("Previous page: " + currentPageIndex);
    }

    /**
     * When the user have clicked the Cancel button, this method is invoked.
     * TODO: Implement logic for restarting/leaving the form page.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onCancel(Button.ClickEvent event) {
        System.out.println("Cancel process");
    }

    /**
     * Displays the next page of the form.
     * If there is no next page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onNext(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex+1 < pages.size() ? currentPageIndex+1 : currentPageIndex);
        showPage(currentPageIndex);
        System.out.println("Next page: " + currentPageIndex);
    }

    /**
     * When the user have clicked the Export button, this method is invoked.
     * TODO: Implement logic for exporting all form data to xml.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onExport(Button.ClickEvent event) {
        System.out.println("Export xml");
    }
}
