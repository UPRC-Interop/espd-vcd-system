/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.model.*;

public class Start extends Master {

    private static final long serialVersionUID = 2425920957238921278L;

    private final Label authInfoLabel = new Label("");
    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();

    public Start(Navigator navigator) {
        super(navigator, true);

        //pageContent.addComponent(authInfoLabel);
        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);
        pagePanel.setSizeFull();

        pageContent.setWidth("100%");
        pageContent.setSizeFull();
        pageContent.setStyleName("start-pageContent");


        CADetails caDetails = new CADetails();
        caDetails.setCACountry("Sweden");

        ESPDRequest espdRequest = new SimpleESPDRequest();
        espdRequest.setCADetails(caDetails);
        espdRequest.setCriterionList(new ESPDBuilder().getCriteriaList());

        // Cenerate the espd request form base on the provided espd request model
        ESPDRequestForm espdRequestForm = new ESPDRequestForm(espdRequest);
        pageContent.addComponent(espdRequestForm);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }
}
