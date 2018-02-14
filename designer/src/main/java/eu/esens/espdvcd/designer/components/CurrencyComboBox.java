package eu.esens.espdvcd.designer.components;

import com.vaadin.ui.ComboBox;
import eu.esens.espdvcd.codelist.CodelistsV1;
import org.apache.commons.lang3.text.WordUtils;

public class CurrencyComboBox extends ComboBox<String> {

    public CurrencyComboBox(String title) {
        super(title, CodelistsV1.Currency.getDataMap().keySet());
        this.setItemCaptionGenerator(i -> WordUtils.capitalize(CodelistsV1.Currency.getValueForId(i)));
        this.setPlaceholder("Currency");
    }
}
