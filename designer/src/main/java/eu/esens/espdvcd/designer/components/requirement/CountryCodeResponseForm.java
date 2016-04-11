package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.AmountResponse;
import eu.esens.espdvcd.model.requirement.CountryCodeResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class CountryCodeResponseForm extends FormLayout {
    private CountryCodeResponse countryCodeResponse = null;
    private TextField countryCode = new TextField("Country Code: ");

    public CountryCodeResponseForm(CountryCodeResponse countryCodeResponse) {
        this.countryCodeResponse = countryCodeResponse;
        addComponent(countryCode);

        // Bind fields
        final BeanFieldGroup<CountryCodeResponse> binder = new BeanFieldGroup<>(CountryCodeResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.countryCodeResponse);
        binder.setBuffered(false);
    }
}
