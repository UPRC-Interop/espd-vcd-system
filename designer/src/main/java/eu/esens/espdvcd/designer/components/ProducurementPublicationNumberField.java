package eu.esens.espdvcd.designer.components;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.TextField;

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
    }
}
