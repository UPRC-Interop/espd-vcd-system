package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import eu.esens.espdvcd.builder.ModelBuilder;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.model.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Start extends Master {

    private static final long serialVersionUID = 2425920957238921278L;

    private final Label authInfoLabel = new Label("");
    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();
    private ESPDRequest espdRequest = null;

    public Start(Navigator navigator) {
        super(navigator, true);

        try {
            pagePanel.setContent(pageContent);
            mainContent.addComponent(pagePanel);
            pagePanel.setSizeFull();
            
            pageContent.setWidth("100%");
            pageContent.setSizeFull();
            pageContent.setStyleName("start-pageContent");
            
            
            CADetails caDetails = new CADetails();
            caDetails.setCACountry("Sweden");
            
            espdRequest = new ModelBuilder().withCADetailsFrom(caDetails).addDefaultESPDCriteriaList().createESPDRequest();
            
            // Cenerate the espd request form base on the provided espd request model
            ESPDRequestForm espdRequestForm = new ESPDRequestForm(this, espdRequest, true);
            pageContent.addComponent(espdRequestForm);
        } catch (BuilderException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }
}
