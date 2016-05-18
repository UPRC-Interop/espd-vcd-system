package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
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
        final BeanFieldGroup<CountryCodeResponse> binder = new BeanFieldGroup<>(CountryCodeResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.countryCodeResponse);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);
    }
}
