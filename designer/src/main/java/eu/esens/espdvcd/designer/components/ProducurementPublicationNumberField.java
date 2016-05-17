package eu.esens.espdvcd.designer.components;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.TextField;

import java.util.regex.Pattern;

/**
 * Created by ixuz on 4/5/16.
 */
public class ProducurementPublicationNumberField extends TextField {
    private String templateValue = "____/S ___-______";
    private String lastAcceptedValue = "____/S ___-______";
    private ProducurementPublicationNumberField thisField;
    private int cursorPosition = 0;

    public ProducurementPublicationNumberField(String caption) {
        super(caption);
        thisField = this;

        this.setTextChangeEventMode(TextChangeEventMode.EAGER);
        this.setNullRepresentation("");
        this.setInputPrompt("____/S ___-______");

        RegexpValidator regexpValidator = new RegexpValidator("\\d{4}\\/S\\s\\d{3}-\\d{6}", "Incorrect format. Example: \"1234/S 123-123456\"");
        this.addValidator(regexpValidator);
/*
        this.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                String text = event.getText();

                if (lastAcceptedValue == text) {
                    return;
                }

                System.out.println("Cursor position: " + thisField.getCursorPosition());
                System.out.println("Text length: " + text.length());

                int curPos = thisField.getCursorPosition();

                if (Pattern.matches("[\\d_]{0,4}", text)) {
                    lastAcceptedValue = text;

                    if (text.length() == 4 && curPos == 4) {
                        text += "/S";
                    }
                } else {
                    setValue(lastAcceptedValue);
                }
            }
        });*/
    }
}
