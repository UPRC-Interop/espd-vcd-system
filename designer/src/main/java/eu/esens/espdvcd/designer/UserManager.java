/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer;

import com.vaadin.server.VaadinSession;

public class UserManager {
    /**
     * Attempt a login using username and password credentials
     */
    public static UserContext login(String username, String password) {
        // Verify provided credentials
        // TODO: Implement real credentials verification instead of mockup credential verification
        if (username.equals("abc") && password.equals("123")) {
            // Return a mockup UserContext
            // TODO: Implement real UserContext instead of mockup UserContext
            UserContext userContext = new UserContext("abc", "Alpha", "Bravo");
            setAuthenticated(true, userContext);
            return userContext;
        }
        return null;
    }

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
     * @param userContext details about this specific user
     */
    private static void setAuthenticated(boolean authenticated, UserContext userContext) {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("auth", authenticated);
        session.setAttribute("userContext", userContext);
    }

    /**
     * Retrieve the current UserContext
     *
     * @return UserContext from the Session
     */
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
