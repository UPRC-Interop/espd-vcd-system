package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.model.LegislationReference;

/**
 * Created by ixuz on 3/7/16.
 */
public class LegislationReferenceForm extends VerticalLayout {

    private TextField title = new TextField("Title");
    private TextField description = new TextField("Title");
    private TextField jurisdictionLevelCode = new TextField("Title");
    private TextField article = new TextField("Title");
    private TextField URI = new TextField("Title");

    public LegislationReferenceForm(LegislationReference legislationReference) {
        addComponent(title);
        addComponent(description);
        addComponent(jurisdictionLevelCode);
        addComponent(article);
        addComponent(URI);
    }
}
