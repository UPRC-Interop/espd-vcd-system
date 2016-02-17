/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import eu.esens.espdvcd.designer.UserContext;
import eu.esens.espdvcd.designer.components.Form;
import eu.esens.espdvcd.designer.FormFactory;
import eu.esens.espdvcd.designer.UserManager;

public class Start extends Master implements View {
    private Navigator navigator = null;
    private Label authInfoLabel = new Label("");

    public Start(Navigator navigator) {
        super(navigator);

        Form form = new Form(FormFactory.SampleEspdTemplate());
        content.addComponent(form);

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
        };

        authInfoLabel.setCaption(authInfo);
        authInfoLabel.setVisible(true);
    }
}
