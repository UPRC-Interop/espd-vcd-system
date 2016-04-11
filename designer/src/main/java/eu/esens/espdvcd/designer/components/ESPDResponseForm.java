package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;

/**
 * Created by ixuz on 4/8/16.
 */
public class ESPDResponseForm extends ESPDForm {

    public ESPDResponseForm(Master view, ESPDRequest espdRequest) {
        super(view, espdRequest);

        // Page 1
        VerticalLayout page1 = newPage("Page 1 Title", "Page 1");
        page1.addComponent(new Label("Page 1 test label"));
    }
}
