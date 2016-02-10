/**
 * Created by ixuz on 2/10/16.
 */

package eu.esens.espdvcd.designer;

import com.vaadin.server.VaadinSession;

public class LoginManager {
    public static boolean isAuthenticated() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null && session.getAttribute("auth") != null && session.getAttribute("auth").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public static void setAuthenticated(boolean isAuthenticated) {
        VaadinSession session = VaadinSession.getCurrent();
        if (isAuthenticated) {
            session.setAttribute("auth", "true");
        } else {
            session.setAttribute("auth", "false");
        }
    }
}
