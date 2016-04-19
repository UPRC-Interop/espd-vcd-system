package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.model.requirement.response.CountryCodeResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class CountryCodeResponseForm extends ResponseForm {
    private CountryCodeResponse countryCodeResponse = null;
    private CountryComboBox countryCode = new CountryComboBox("Country Code: ");

    public CountryCodeResponseForm(CountryCodeResponse countryCodeResponse, String caption) {
        this.countryCodeResponse = countryCodeResponse;
        addComponent(countryCode);
        countryCode.setCaption(caption);
        countryCode.setWidth(280, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<CountryCodeResponse> binder = new BeanFieldGroup<>(CountryCodeResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.countryCodeResponse);
        binder.setBuffered(false);
    }
}
