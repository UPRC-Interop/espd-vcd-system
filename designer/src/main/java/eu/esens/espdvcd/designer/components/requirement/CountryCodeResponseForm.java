package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.model.requirement.response.CountryCodeResponse;

public class CountryCodeResponseForm extends ResponseForm {
    private CountryCodeResponse countryCodeResponse = null;
    private CountryComboBox countryCode = new CountryComboBox("Country Code: ");

    public CountryCodeResponseForm(CountryCodeResponse countryCodeResponse, String caption, boolean readOnly) {
        this.countryCodeResponse = countryCodeResponse;
        addComponent(countryCode);
        countryCode.setCaption(caption);
        countryCode.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<CountryCodeResponse> binder = new BeanValidationBinder<>(CountryCodeResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(countryCodeResponse);
        binder.setReadOnly(readOnly);
    }
}
