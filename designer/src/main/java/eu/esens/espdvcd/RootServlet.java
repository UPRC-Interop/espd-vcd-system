/**
 * Created by ixuz on 1/12/16.
 */

package eu.esens.espdvcd;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import javax.servlet.annotation.*;

@WebServlet (
    value = {
        "/*",
        "/VAADIN/*"
    },
    asyncSupported = true
)

@VaadinServletConfiguration(
    productionMode = false,
    ui = Designer.class
)

public class RootServlet extends VaadinServlet {}
