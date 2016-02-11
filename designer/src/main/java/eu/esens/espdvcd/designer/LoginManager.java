/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.designer.UserContext;
import com.vaadin.server.VaadinSession;

public class LoginManager {
    /**
     * Checks if the user session is fully authenticated and valid
     *
     * @return true if the user session is verified to be valid
     */
    public static boolean isAuthenticated() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null && session.getAttribute("auth") != null && session.getAttribute("auth").equals(true)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Changes the state of the authentication.
     *
     * @param authenticated True sets the user session to be authenticated. False invalidates the authentication.
     */
    public static void setAuthenticated(boolean authenticated) {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("auth", authenticated);

        // TODO: Implement real UserContext instead of mockup UserContext
        UserContext userContext = new UserContext("abc", "Alpha", "Bravo");
        session.setAttribute("auth", authenticated);
        session.setAttribute("userContext", userContext);
    }

    public static UserContext getUserContext() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            UserContext userContext = (UserContext) session.getAttribute("userContext");
            if (userContext != null)
            return userContext;
        }
        return null;
    }
}
