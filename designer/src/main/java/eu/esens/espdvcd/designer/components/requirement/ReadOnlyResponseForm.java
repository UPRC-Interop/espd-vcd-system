package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;

/**
 * Created by ixuz on 4/25/16.
 */
public class ReadOnlyResponseForm extends ResponseForm {
    protected Label text = new Label("Text: ");

    public ReadOnlyResponseForm(String text, String caption) {
        //addComponent(text);
        //text.setCaption(caption);
        //text.setNullRepresentation("");
        //text.setWidth(280, Sizeable.Unit.PIXELS);
    }
}
