/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.components.CADetailsForm;
import eu.esens.espdvcd.model.ESPDRequestImpl;

public class Start extends Master {
    private final Label authInfoLabel = new Label("");
    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();

    public Start(Navigator navigator) {
        super(navigator, true);

        ESPDRequestImpl espdRequest = new ESPDRequestImpl();
        pageContent.addComponent(new CADetailsForm(espdRequest));

        // Display espd request xml button
        pageContent.addComponent(new Button("Print ESPD Request XML!",
                (Button.ClickEvent event) -> {
                    ESPDBuilder espdBuilder = new ESPDBuilder();
                    String xml = espdBuilder.createXMLasString(espdRequest);
                    System.out.println("Xml: " + xml);
                }
        ));

        //pageContent.addComponent(authInfoLabel);
        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        /*
        if (!UserManager.isAuthenticated()) {
            authInfoLabel.setVisible(false);
            getNavigator().navigateTo("login");
            return;
        }

        // Update the auth info fields
        UserContext userContext = UserManager.getUserContext();

        String authInfo = "";
        authInfo += userContext.getFirstName() + " " + userContext.getLastName() + " authenticated as: ";
        switch (userContext.getRole()) {
            case "CA": authInfo += "'Contractive Authority'"; break;
            case "EO": authInfo += "'Economic Operator'"; break;
            default: authInfo += "'Regular User'"; break;
        }

        authInfoLabel.setCaption(authInfo);
        authInfoLabel.setVisible(true);*/
    }
}
