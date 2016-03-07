/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.designer.UserContext;
import eu.esens.espdvcd.designer.components.CADetailsForm;
import eu.esens.espdvcd.designer.UserManager;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SimpleESPDRequest;

public class Start extends Master implements View {

    private static final long serialVersionUID = 2425920957238921278L;
   
    private final Navigator navigator = null;
    private final Label authInfoLabel = new Label("");

    public Start(Navigator navigator) {
        super(navigator);

        ESPDRequest espdRequest = new SimpleESPDRequest();
        content.addComponent(new CADetailsForm(espdRequest));

        // Display espd request xml button
        content.addComponent(new Button("Print ESPD Request XML!",
                (Button.ClickEvent event) -> {
                    ESPDBuilder espdBuilder = new ESPDBuilder();
                    String xml = espdBuilder.createXMLasString(espdRequest);
                    System.out.println("Xml: " + xml);
                }
        ));

        content.addComponent(authInfoLabel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
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
        authInfoLabel.setVisible(true);
    }
}
