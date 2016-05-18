package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.ui.Label;

public class ReadOnlyResponseForm extends ResponseForm {
    protected Label label = new Label("Text: ");

    public ReadOnlyResponseForm(String text, String caption) {
        addStyleName("ReadOnlyResponseForm");
        label.setCaption(caption);
        label.setValue(text);
        label.setSizeUndefined();
        addComponent(label);
    }
}
