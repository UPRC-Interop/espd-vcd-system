package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.data.fieldgroup.*;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import eu.esens.espdvcd.designer.UserManager;
import eu.esens.espdvcd.designer.components.CADetailsForm;
import eu.esens.espdvcd.designer.components.LoginFormWindow;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDRequestImpl;
import eu.esens.espdvcd.model.CADetails;

import java.io.InputStream;

/**
 * Created by ixuz on 2/23/16.
 */

public class Sandbox extends Master implements View {
    private Navigator navigator = null;

    public Sandbox(Navigator navigator) {
        super(navigator);

        content.addComponent(new Label("This is the sandbox!"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
